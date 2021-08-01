import { axiosInstance } from '../Service/networkProvider';

export const getProduct = (id) => {
  return axiosInstance('get', `api/v1/adv/${id}`);
};
