import { obminyashkaApi } from "../Service/networkProvider";

interface RequestData {
  page: number;
  size: number;
  age: string[];
  gender: string;
  keyword: string;
  season: string[];
  categoryId: number;
  locationId: string;
  shoesSizes: string[];
  enableRandom: boolean;
  clothingSizes: string[];
  excludeAdvertisementId: string;
  subcategoriesIdValues: number[];
}

export const postFilter = (dataValue: RequestData) =>
  obminyashkaApi.post("/adv/filter", dataValue).then(({ data }) => data);
