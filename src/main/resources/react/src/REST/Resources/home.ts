/* eslint-disable */
// @ts-nocheck
// TODO: fix typescript
import { obminyashkaApi } from '../Service/networkProvider';

export const getCurrentOffers = () => obminyashkaApi.get('/adv/thumbnail/random');
