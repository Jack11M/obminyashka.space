import { axiosInstance } from '../Service/networkProvider';

export const getUserInfo = () => {
  return axiosInstance('get', '/user/my-info');
};

export const putUserInfo = (data) => {
  return axiosInstance('put', '/user/my-info', data);
};

export const postUserChildren = (data) => {
  return axiosInstance('post', '/user/child', data);
};

export const putUserChildren = (data) => {
  return axiosInstance('put', '/user/child', data);
};

export const deleteUserChildren = (id) => {
  return axiosInstance('delete', `/user/child/${id}`);
};

export const putPasswordFetch = (data) => {
  return axiosInstance('put', `/user/service/pass/`, data);
};
export const putEmailFetch = (data) => {
  return axiosInstance('put', `/user/service/email/`, data);
};
