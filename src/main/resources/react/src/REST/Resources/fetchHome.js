import { axiosInstance } from '../Service/networkProvider';

export const getCurrentOffers = () => {
  return axiosInstance('get', `/adv/thumbnail/?page=0&size=12`)
}
