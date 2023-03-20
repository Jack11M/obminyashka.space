import axios from 'axios';
import { store } from 'store';

import { logOutUser } from 'store/auth/slice';
import { setStorageUser, getStorageUser, getStorageLang } from 'Utils';

const refreshUrl = '/auth/refresh/token';
const array = ['/user/my-info', '/user/child', '/user/my-adv'];

function handleAuthError(error, onAuthError) {
  if (onAuthError) onAuthError();
  return Promise.reject(error);
}

const isProvideToken = ({ method, url }) =>
  method === 'get' && !array.includes(url);

function initObminyashka({ onAuthError }) {
  axios.defaults.baseURL = '/api/v1';
  axios.defaults.headers = {
    'Content-Type': 'application/json',
  };

  axios.interceptors.request.use((config) => {
    const token =
      getStorageUser('user').access_token ||
      sessionStorage.getItem('code') ||
      '';

    const newConfig = { ...config };
    newConfig.headers['accept-language'] = getStorageLang();

    if (token) {
      newConfig.headers.Authorization = `Bearer ${token}`;
    }

    if (newConfig.headers.is_access) {
      delete newConfig.headers.Authorization;
      newConfig.headers.is_access = false;
    }

    if (isProvideToken({ method: newConfig.method, url: newConfig.url })) {
      delete newConfig.headers.Authorization;
    }

    return newConfig;
  });

  axios.interceptors.response.use(
    (response) => response,

    async (error) => {
      const originalRequest = error.config;
      const refreshToken = getStorageUser('user').refresh_token;

      if (!refreshToken && error?.response?.status === 401) {
        return handleAuthError(error, onAuthError);
      }

      if (error?.response?.status === 401) {
        delete originalRequest.headers.Authorization;

        if (error.response.config.url === refreshUrl) {
          return handleAuthError(error, onAuthError);
        }

        return axios
          .post(
            refreshUrl,
            {},
            {
              headers: {
                refresh: `Bearer ${refreshToken}`,
                is_access: true,
              },
            }
          )
          .then((res) => {
            if (res.status === 200) {
              setStorageUser(res.data);
              return axios(originalRequest);
            }

            return handleAuthError(error, onAuthError);
          })
          .catch(() => handleAuthError(error, onAuthError));
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
