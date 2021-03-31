import axios from 'axios';
import { getStorageUser, getStorageLang } from '../../redux/Utils';


const instance = axios.create( {
	baseURL: 'http://localhost:8080/',
	headers: {
		'Content-Type': 'application/json'
	}
} );

export const axiosInstance = async ( method, url, body = null ) => {
	instance.defaults.headers.common['Authorization'] = 'Bearer ' + getStorageUser( 'token' );
	instance.defaults.headers.common['accept-language'] = getStorageLang();
	return await instance[method]( `${ url }`, body );
};
