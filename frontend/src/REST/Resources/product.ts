import { obminyashkaApi } from '../Service/networkProvider';

export const getProduct = (id: string) => obminyashkaApi.get(`/adv/${id}`).then(({ data }) => data);

export const getLocation = (id: string) =>
  obminyashkaApi.get(`/location/${id}`).then(({ data }) => data);
