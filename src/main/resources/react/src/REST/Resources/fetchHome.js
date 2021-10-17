import { obminyashkaApi } from '../Service/networkProvider';

export const getCurrentOffers = () => {
  return obminyashkaApi.get(`/adv/thumbnail/random`);
};
