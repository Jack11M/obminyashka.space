import ru from './ru';
import ua from './ua';
import en from './en';
import { useSelector } from 'react-redux';


const translations = { ru, ua, en };

const getTranslatedText = (key) => {
	const arr = key.split('.')
	// eslint-disable-next-line react-hooks/rules-of-hooks
	const lang = useSelector(state => state.lang)
const currentTranslation = translations[lang]
	console.log(currentTranslation[arr[0]][arr[1]]);
	return currentTranslation[arr[0]][arr[1]]
}

export {getTranslatedText}

