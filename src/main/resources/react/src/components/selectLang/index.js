import { useNavigate } from 'react-router-dom';
import { useDispatch, useSelector } from 'react-redux';

import { setLanguage, getAuth } from 'store/auth/slice';

import * as Styles from './styles';

const LanguageSelection = () => {
  const dispatch = useDispatch();
  const navigate = useNavigate();
  const { lang } = useSelector(getAuth);

  const languageArray = ['ua', 'en'];

  const handleSelected = (langValue) => {
    dispatch(setLanguage(langValue));
    navigate(0);
  };

  return (
    <Styles.LanguagePanel>
      {languageArray.map((el) => (
        <Styles.LanguageItem
          key={el}
          checked={el === lang}
          onClick={() => handleSelected(el)}
        >
          {el}
        </Styles.LanguageItem>
      ))}
    </Styles.LanguagePanel>
  );
};

export { LanguageSelection };
