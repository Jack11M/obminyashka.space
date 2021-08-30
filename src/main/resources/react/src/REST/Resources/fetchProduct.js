import { axiosInstance } from '../Service/networkProvider';

export const getProduct = (id) => {
  return axiosInstance('get', `/adv/${id}`);
};
