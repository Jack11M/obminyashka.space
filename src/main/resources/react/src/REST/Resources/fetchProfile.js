import { axiosInstance } from '../Service/networkProvider';

export const getUserInfo = () => {
	return axiosInstance('get', 'user', {categories: 'my-info'});
};
export const putUserInfo = (body) => {
	return axiosInstance('put', 'user', {categories: 'info', body});
};
