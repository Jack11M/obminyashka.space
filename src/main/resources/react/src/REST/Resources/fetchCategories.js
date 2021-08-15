import { axiosInstance } from '../Service/networkProvider';

export const getCategoryAll = () => {
	return axiosInstance('get', 'api/v1/category/all');
};


