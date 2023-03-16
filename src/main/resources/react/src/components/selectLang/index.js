import { useNavigate } from 'react-router-dom';
import { useDispatch, useSelector } from 'react-redux';
import { LanguageSelection } from 'obminyashka-components';

import { setLanguage, getAuth } from 'store/auth/slice';

const SelectLanguage = () => {
  const dispatch = useDispatch();
  const navigate = useNavigate();
  const { lang } = useSelector(getAuth);

  const handleSelected = (langValue) => {
    dispatch(setLanguage(langValue.lang));
    navigate(0);
  };

  return <LanguageSelection lang={lang} onClick={handleSelected} />;
};

export { SelectLanguage };
