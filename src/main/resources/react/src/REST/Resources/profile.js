import { obminyashkaApi } from '../Service/networkProvider';

export const getUserInfo = () =>
  obminyashkaApi.get('/user/my-info').then(({ data }) => data);

export const putUserInfo = (payload) =>
  obminyashkaApi.put('/user/my-info', payload).then(({ data }) => data);

export const postUserChildren = (children) =>
  obminyashkaApi.post('/user/child', children).then(({ data }) => data);

export const putUserChildren = (children) =>
  obminyashkaApi.put('/user/child', children).then(({ data }) => data);

export const deleteUserChildren = (id) =>
  obminyashkaApi.delete(`/user/child/${id}`).then(({ data }) => data);

export const putPasswordFetch = (password) =>
  obminyashkaApi.put('/user/service/pass/', password).then(({ data }) => data);

export const putEmailFetch = (email) =>
  obminyashkaApi.put('/user/service/email/', email).then(({ data }) => data);
