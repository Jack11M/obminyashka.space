import { obminyashkaApi } from '../Service/networkProvider';

export const getProduct = (id) =>
  obminyashkaApi.get(`/adv/${id}`).then(({ data }) => data);

export const getLocation = (id) =>
  obminyashkaApi.get(`/location/${id}`).then(({ data }) => data);
