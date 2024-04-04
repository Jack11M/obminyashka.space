import { obminyashkaApi } from "../Service/networkProvider";

interface RequestData {
  page: number;
  size: number;
  enableRandom: boolean;
  keyword: string;
  subcategoriesIdValues: number[];
  excludeAdvertisementId: string;
  locationId: string;
  gender: string;
  age: string[];
  clothingSizes: string[];
  shoesSizes: string[];
  season: string[];
}

export const postFilter = (dataValue: RequestData) =>
  obminyashkaApi.post("/adv/filter", dataValue).then(({ data }) => data);
