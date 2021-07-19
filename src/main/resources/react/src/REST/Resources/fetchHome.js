import { axiosInstance } from '../Service/networkProvider';

export const getCurrentOffers = () => {
  return axiosInstance('get', `api/v1/adv/thumbnail`);
};
