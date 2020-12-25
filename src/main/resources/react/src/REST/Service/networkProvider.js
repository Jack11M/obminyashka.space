import axios from 'axios';
import { getStorage, getStorageLang } from '../../redux/Utils';


const instance = axios.create( {
	baseURL: 'http://localhost:8080',
	headers: {
		'Content-Type': 'application/json'
	}
} );

export const axiosInstance = async ( method, url, { id = '', ...body } ) => {
	instance.defaults.headers.common['Authorization'] = 'Bearer ' + getStorage( 'token' );
	instance.defaults.headers.common['accept-language'] = getStorageLang();
console.log(	body)
	return await instance[method]( `${ url }/${ id }`, body );
};
