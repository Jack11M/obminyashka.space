import axios from 'axios';
import { store } from 'store';

import { setStorageUser, getStorageUser, getStorageLang } from 'Utils';
import { logOutUser } from '../../store/auth/slice';

const refreshUrl = '/auth/refresh/token';

function handleAuthError(error, onAuthError) {
  console.log('logout');
  if (onAuthError) onAuthError();
  return Promise.reject(error);
}

function initObminyashka({ onAuthError }) {
  axios.defaults.baseURL = process.env.REACT_APP_API_URL;
  axios.defaults.headers = {
    'Content-Type': 'application/json',
  };

  axios.interceptors.request.use((config) => {
    const token = getStorageUser('user').accessToken;
    const newConfig = { ...config };
    if (token) newConfig.headers.Authorization = 'Bearer ' + token;
    else delete newConfig.headers.Authorization;
    newConfig.headers['accept-language'] = getStorageLang();
    return newConfig;
  });

  axios.interceptors.response.use(
    (response) => {
      return response;
    },
    async (error) => {
      const originalRequest = error.config;
      const refreshToken = getStorageUser('user').refreshToken;

      if (!refreshToken && error?.response?.status === 401) {
        return handleAuthError(error, onAuthError);
      }
      if (
        error?.response?.status === 401 &&
        error?.response.data.startsWith('JWT')
      ) {
        if (!refreshToken) return handleAuthError(error, onAuthError);

        delete originalRequest.headers.Authorization;
        return axios
          .post(
            refreshUrl,
            {},
            {
              headers: { RefreshToken: 'Bearer ' + refreshToken },
            }
          )
          .then((res) => {
            if (res.status === 200) {
              setStorageUser(res.data);
              return axios(originalRequest);
            }
            return handleAuthError(error, onAuthError);
          })
          .catch(() => {
            return handleAuthError(error, onAuthError);
          });
      }
      return Promise.reject(error);
    }
  );
  return axios;
}
const obminyashkaApi = initObminyashka({
  onAuthError: () => store.dispatch(logOutUser()),
});

export { obminyashkaApi };
