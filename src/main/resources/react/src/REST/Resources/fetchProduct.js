import { obminyashkaApi } from '../Service/networkProvider';

export const getProduct = (id) => {
  return obminyashkaApi.get( `/adv/${id}`);
};
