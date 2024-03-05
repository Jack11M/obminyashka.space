import { obminyashkaApi } from '../Service/networkProvider';

export const getCurrentOffers = () => obminyashkaApi.post('/adv/filter', { enableRandom: true});
