import { obminyashkaApi } from '../Service/networkProvider';

export const getCategoryAll = () => {
  return obminyashkaApi.get('/category/all').then(({ data }) => data);
};

export const getLocationLanguageAll = () => {
  return obminyashkaApi.get('/location/all').then(({ data }) => data);
};

export const getSize = (id) => {
  return obminyashkaApi.get(`/category/${id}/sizes`).then(({ data }) => data);
};
