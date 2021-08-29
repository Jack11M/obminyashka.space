import { axiosInstance } from '../Service/networkProvider';

export const getCurrentOffers = () => {
  return axiosInstance('get', `/adv/thumbnail`);
};
