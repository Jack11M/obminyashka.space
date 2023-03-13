import { useNavigate } from 'react-router-dom';
import { useDispatch, useSelector } from 'react-redux';
import { LanguageSelection } from 'obminyashka-components';

import { setLanguage, getAuth } from 'store/auth/slice';

const SelectLanguage = () => {
  const dispatch = useDispatch();
  const navigate = useNavigate();
  const { lang } = useSelector(getAuth);

  const languageArray = ['ua', 'en'];

  const handleSelected = (langValue) => {
    dispatch(setLanguage(langValue));
    navigate(0);
  };

  return (
    <LanguageSelection
      lang={lang}
      onClick={() =>
        handleSelected(languageArray.filter((el) => lang !== el).toString())
      }
    />
  );
};

export { SelectLanguage };
