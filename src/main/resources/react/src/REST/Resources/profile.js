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

export const putAvatar = (avatar) =>
  obminyashkaApi.put('/user/service/avatar', avatar).then(({ data }) => data);

export const deleteAvatar = () =>
  obminyashkaApi.delete('/user/service/avatar').then(({ data }) => data);

export const deleteUserAccount = (userData) =>
  obminyashkaApi
    .delete('/user/service/delete', { data: userData })
    .then(({ data }) => data);
