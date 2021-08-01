import { axiosInstance } from '../Service/networkProvider';

export const getUserInfo = () => {
  return axiosInstance('get', 'api/v1/user/my-info');
};

export const putUserInfo = (data) => {
  return axiosInstance('put', 'api/v1/user/my-info', data);
};

export const postUserChildren = (data) => {
  return axiosInstance('post', 'api/v1/user/child', data);
};

export const putUserChildren = (data) => {
  return axiosInstance('put', 'api/v1/user/child', data);
};

export const deleteUserChildren = (id) => {
  return axiosInstance('delete', `api/v1/user/child/${id}`);
};

export const putPasswordFetch = (data) => {
  return axiosInstance('put', `api/v1/user/service/pass/`, data);
};
export const putEmailFetch = (data) => {
  return axiosInstance('put', `api/v1/user/service/email/`, data);
};
