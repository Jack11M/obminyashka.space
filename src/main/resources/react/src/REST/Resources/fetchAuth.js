import { axiosInstance } from '../Service/networkProvider';

export const postAuthLogin = ( data ) => {
	return axiosInstance( 'post', 'auth/login', data );
};

export const postAuthRegister = ( data ) => {
	return axiosInstance( 'post', 'auth/register', data );
};

export const postAuthLogout = () => {
	return axiosInstance( 'post', '/auth/logout' );
};
