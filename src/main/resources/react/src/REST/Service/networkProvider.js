import axios from 'axios';
import { store } from 'store';

import { logOutUser } from 'store/auth/slice';
import { setStorageUser, getStorageUser, getStorageLang } from 'Utils';

const refreshUrl = '/auth/refresh/token';

function handleAuthError(error, onAuthError) {
  if (onAuthError) onAuthError();
  return Promise.reject(error);
}

function initObminyashka({ onAuthError }) {
  axios.defaults.baseURL = `https://localhost/${process.env.REACT_APP_API_URL}`
  axios.defaults.headers = {
    'Content-Type': 'application/json',
  };

  axios.interceptors.request.use((config) => {
    const token = getStorageUser('user').access_token ||
        sessionStorage.getItem('code') || '';
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
      const refreshToken = getStorageUser('user').refresh_token;

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
              headers: { refresh_token: 'Bearer ' + refreshToken },
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
