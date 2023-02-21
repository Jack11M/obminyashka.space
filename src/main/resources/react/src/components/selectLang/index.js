import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useDispatch, useSelector } from 'react-redux';

import { setLanguage, getAuth, setChangeLang } from 'store/auth/slice';

import * as Styles from './styles';

const LanguageSelection = () => {
  const dispatch = useDispatch();
  const navigate = useNavigate();
  const { lang, isChangeLang } = useSelector(getAuth);
  const [languageArray, setLanguageArray] = useState([
    { value: 'ua', checked: false },
    { value: 'en', checked: false },
  ]);

  useEffect(() => {
    if (isChangeLang) {
      dispatch(setChangeLang(false));
      navigate(0);
    }

    const newLang = languageArray.map((item) =>
      item.value === lang
        ? { ...item, checked: true }
        : { ...item, checked: false }
    );

    setLanguageArray(newLang);
  }, [lang]);

  const handleSelected = (langValue) => {
    dispatch(setLanguage(langValue));
    dispatch(setChangeLang(true));
  };

  return (
    <Styles.LanguagePanel>
      {languageArray.map((option) => (
        <Styles.LanguageItem
          key={option.value}
          checked={option.checked}
          onClick={() => handleSelected(option.value)}
        >
          {option.value}
        </Styles.LanguageItem>
      ))}
    </Styles.LanguagePanel>
  );
};

export { LanguageSelection };
