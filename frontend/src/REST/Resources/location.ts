import { obminyashkaApi } from "../Service/networkProvider";

export const getAreaLocation = () =>
  obminyashkaApi.get(`/location/area`).then(({ data }) => data);

export const getCityLocation = (id: string) =>
  obminyashkaApi.get(`/location/city?areaId=${id}`).then(({ data }) => data);
