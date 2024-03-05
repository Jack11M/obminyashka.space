import { obminyashkaApi } from '../Service/networkProvider';

export const postFilter = (dataValue: { keyword: number, page: number}) =>
  obminyashkaApi.post('/adv/filter', dataValue).then(({ data }) => data);
