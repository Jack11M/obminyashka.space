import { obminyashkaApi } from '../Service/networkProvider';

export const sendNewAdv = (payload) =>
  obminyashkaApi.post('/adv', payload).then(({ data }) => data);

export const getCategoryAll = () =>
  obminyashkaApi.get('/category/all').then(({ data }) => data);

export const getLocationLanguageAll = () =>
  obminyashkaApi.get('/location/all').then(({ data }) => data);

export const getSize = (id) =>
  obminyashkaApi.get(`/category/${id}/sizes`).then(({ data }) => data);
