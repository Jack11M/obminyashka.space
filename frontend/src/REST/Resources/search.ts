/* eslint-disable */
// @ts-nocheck
// TODO: fix typescript
import { obminyashkaApi } from '../Service/networkProvider';

export const getSearch = (searchValue, page = 0, size = 12) =>
  obminyashkaApi
    .get(`/adv/search/${searchValue}`, { params: { page, size } })
    .then(({ data }) => data);

export const postFilter = (dataValue) =>
  obminyashkaApi.post('/adv/filter', dataValue).then(({ data }) => data);
