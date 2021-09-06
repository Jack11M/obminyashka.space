import { obminyashkaApi } from '../Service/networkProvider';

export const getUserInfo = () => {
  return obminyashkaApi.get('/user/my-info');
};

export const putUserInfo = (data) => {
  return obminyashkaApi.put('/user/my-info', data);
};

export const postUserChildren = (data) => {
  return obminyashkaApi.post('/user/child', data);
};

export const putUserChildren = (data) => {
  return obminyashkaApi.put('/user/child', data);
};

export const deleteUserChildren = (id) => {
  return obminyashkaApi.delete(`/user/child/${id}`);
};

export const putPasswordFetch = (data) => {
  return obminyashkaApi.put('/user/service/pass/', data);
};
export const putEmailFetch = (data) => {
  return obminyashkaApi.put('/user/service/email/', data);
};
