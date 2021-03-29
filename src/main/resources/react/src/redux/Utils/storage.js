export const getStorageUser = ( option ) => {
	const local = JSON.parse( localStorage.getItem( 'user' ) ) || '';
	const session = JSON.parse( sessionStorage.getItem( 'user' ) ) || '';
	return (local && local[option] ) || (session && session[option]) || '';
};

export const getStorageLang = () => {
	return localStorage.getItem( 'lang' ) || 'ru';
};

export const removeTokenFromStorage = () => {
	localStorage.removeItem('user');
	sessionStorage.removeItem('user');
	return false;
}
