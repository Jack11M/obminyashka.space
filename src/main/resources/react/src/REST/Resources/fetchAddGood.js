import { obminyashkaApi } from '../Service/networkProvider';

export const sendNewAdv = (payload) => {
  return obminyashkaApi.post('/adv', payload).then(({ data }) => data);
};

export const getCategoryAll = () => {
  return obminyashkaApi.get('/category/all').then(({ data }) => data);
};

export const getLocationLanguageAll = () => {
  return obminyashkaApi.get('/location/all').then(({ data }) => data);
};

export const getSize = (id) => {
  return obminyashkaApi.get(`/category/${id}/sizes`).then(({ data }) => data);
};
