import axios from 'axios';

const getTokenFromLocalStorage = () => {
	let token = sessionStorage.getItem('user');
	token = JSON.parse(token);
	return token.token;
};

const AUTH_TOKEN = 'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJEcm9uaXNoZSIsInJvbGUiOiJST0xFX1VTRVIiLCJpYXQiOjE1OTk5OTEyOTMsImV4cCI6MTYwMDAwOTI5M30.YsW5BYtuYkomMlzmIlDox2lbI9HKzFzfWJp0rcfU2Bo';

const instance = axios.create({
	baseURL: 'http://localhost:8080',
	headers: {
		'Authorization': 'Bearer ' + AUTH_TOKEN,
		'Content-Type': 'application/json',
		'accept-language': 'en'
	}

});

export const axiosInstance = async (method, resource, {categories = '', id = '', body}) => {
	return instance[method](`/${resource}/${categories}/${id}`, body);
};

