/* eslint-disable */
// @ts-nocheck
// TODO: fix typescript
import { obminyashkaApi } from "../Service/networkProvider";

export const getFilter = (params) =>
  obminyashkaApi.get("adv/filter", { params }).then(({ data }) => data);
