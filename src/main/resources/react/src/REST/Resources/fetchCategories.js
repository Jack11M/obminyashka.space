import { obminyashkaApi } from '../Service/networkProvider';

export const getCategoryAll = () => {
  return obminyashkaApi.get('/category/all').then(({ data }) => data);
};
