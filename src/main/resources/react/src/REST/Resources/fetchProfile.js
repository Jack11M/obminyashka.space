import { axiosInstance } from '../Service/networkProvider';

export const postAuthLogin = ( data ) => {
	return axiosInstance( 'post', 'auth/login', data );
};

export const postAuthRegister = ( data ) => {
	return axiosInstance( 'post', 'auth/register', data );
};

export const getUserInfo = () => {
	return axiosInstance( 'get', 'user/my-info', {} );
};

export const putUserInfo = ( data ) => {
	return axiosInstance( 'put', 'user/info', data );
};

export const getProduct = (id) => {
	return axiosInstance('get', 'adv', null, id)
}

export const createProduct = (data) => {
	return axiosInstance('post', 'adv', data)
}