import { obminyashkaApi } from '../Service/networkProvider';

export const getUserInfo = () => obminyashkaApi.get('/user/my-info');

export const putUserInfo = (payload) =>
  obminyashkaApi.put('/user/my-info', payload).then(({ data }) => data);

export const postUserChildren = (data) =>
  obminyashkaApi.post('/user/child', data);

export const putUserChildren = (data) =>
  obminyashkaApi.put('/user/child', data);

export const deleteUserChildren = (id) =>
  obminyashkaApi.delete(`/user/child/${id}`);

export const putPasswordFetch = (data) =>
  obminyashkaApi.put('/user/service/pass/', data);

export const putEmailFetch = (data) =>
  obminyashkaApi.put('/user/service/email/', data);
