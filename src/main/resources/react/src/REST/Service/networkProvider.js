import axios from 'axios';

const getToken = () => {
	const LOCAL_STORAGE = JSON.parse( localStorage.getItem( 'user' ) ) || '';
	const SESSION_STORAGE = JSON.parse( sessionStorage.getItem( 'user' ) ) || '';
	return LOCAL_STORAGE.token || SESSION_STORAGE.token;
};

const instance = axios.create( {
	baseURL: '',
	headers: {
		'Content-Type': 'application/json'
	}
} );

export const axiosInstance = async ( method, resource, { categories = '', id = '', body } ) => {

	instance.defaults.headers.common['Authorization'] = 'Bearer ' + getToken();
	instance.defaults.headers.common['accept-language'] = 'en';
	return await instance[method]( `/${ resource }/${ categories }/${ id }`, body );
};
