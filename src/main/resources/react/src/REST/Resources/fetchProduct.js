import { obminyashkaApi } from '../Service/networkProvider';

export const getProduct = (id) => obminyashkaApi.get(`/adv/${id}`);
