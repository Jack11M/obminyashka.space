export const getStorage = ( option ) => {
	const local = JSON.parse( localStorage.getItem( 'user' ) ) || '';
	const session = JSON.parse( sessionStorage.getItem( 'user' ) ) || '';
	return (local && local[option] ) || (session && session[option]) || '';
};

export const getStorageLang = () => {
	return localStorage.getItem( 'lang' ) || 'ru';
};