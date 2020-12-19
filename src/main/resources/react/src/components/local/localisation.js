import ru from './ru';
import ua from './ua';
import en from './en';
import { useSelector } from 'react-redux';

const translations = { ru, ua, en };

const getTranslatedText = (key) => {
	const arrayKeys = key.split('.')
	// eslint-disable-next-line react-hooks/rules-of-hooks
	const lang = useSelector(state => state.lang)
const currentTranslation = translations[lang]
	return currentTranslation[arrayKeys[0]][arrayKeys[1]]
}

export {getTranslatedText};
