import { axiosInstance } from '../Service/networkProvider';

export const postAuthLogin = (data) => {
  return axiosInstance('post', 'api/v1/auth/login', data);
};

export const postAuthRegister = (data) => {
  return axiosInstance('post', 'api/v1/auth/register', data);
};

export const postAuthLogout = () => {
  return axiosInstance('post', 'api/v1/auth/logout');
};
