import { obminyashkaApi } from '../Service/networkProvider';

export const getCurrentOffers = () =>
  obminyashkaApi.get('/adv/thumbnail/random');
