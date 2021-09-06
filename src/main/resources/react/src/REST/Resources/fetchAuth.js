import { obminyashkaApi } from '../Service/networkProvider';

export const postAuthLogin = (data) => {
  return obminyashkaApi.post('/auth/login', data).then(({ data }) => data);
};

export const postAuthRegister = (data) => {
  return obminyashkaApi.post('/auth/register', data);
};

export const postAuthLogout = () => {
  return obminyashkaApi.post('/auth/logout');
};
