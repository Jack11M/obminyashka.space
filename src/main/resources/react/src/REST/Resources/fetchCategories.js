import { axiosInstance } from '../Service/networkProvider';

export const getCategoryAll = () => {
  return axiosInstance('get', '/category/all').then(({ data }) => data);
};
