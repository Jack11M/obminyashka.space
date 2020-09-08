import axios from 'axios';

const getTokenFromLocalStorage = () => {
	let token = sessionStorage.getItem('user')
	token = JSON.parse(token)
	return token.token
}

const AUTH_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJEcm9uaXNoZSIsInJvbGUiOiJST0xFX1VTRVIiLCJpYXQiOjE1OTk1ODU0NTUsImV4cCI6MTU5OTYwMzQ1NX0.ZVBG8yIhKCvxEbmWx775kbp2dzEtaazxy-4AYrFX5Qs"

const fetchData = axios.create({
	baseURL: 'http://localhost:8080',
	headers: {
		'Authorization': 'Bearer ' + AUTH_TOKEN,
		"Content-Type": "application/json"
	}

})

export const getFetch = async (method, resource, {categories='',id ='',body}) => {
	return fetchData[method](`/${resource}/${categories}/${id}`, body).catch(error => error.response).then(res => res.data)
}

