import axios from 'axios';
import { getStorageUser, getStorageLang } from 'Utils';

const instance = axios.create({
  baseURL: process.env.REACT_APP_API_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

export const axiosInstance = async (method, url, body = null) => {
  instance.defaults.headers.common['Authorization'] =
    'Bearer ' + getStorageUser('user').token;
  instance.defaults.headers.common['accept-language'] = getStorageLang();
  return await instance[method](`${url}`, body);
};
