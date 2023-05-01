/* eslint-disable */
// @ts-nocheck
// TODO: fix typescript
import { obminyashkaApi } from '../Service/networkProvider';

export const postAuthLogin = (datValue) =>
  obminyashkaApi.post('/auth/login', datValue).then(({ data }) => data);

export const postAuthRegister = (data) => obminyashkaApi.post('/auth/register', data);

export const postAuthLogout = () => obminyashkaApi.post('/auth/logout');

export const postOAuth2Success = () =>
  obminyashkaApi.post('/auth/oauth2/success').then(({ data }) => data);
