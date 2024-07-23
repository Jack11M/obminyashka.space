import { obminyashkaApi } from "../Service/networkProvider";

// export const getProduct = (id: string) =>
//   obminyashkaApi.get(`/adv/${id}`).then(({ data }) => data);

export const getAreaLocation = () =>
  obminyashkaApi.get(`/location/area`).then(({ data }) => data);
