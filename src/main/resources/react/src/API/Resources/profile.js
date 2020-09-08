import {getFetch} from '../Service/networkProvider';

export const getUserInfo = async () => {
	return await getFetch('get', 'user', {categories:'my-info'})
}

