import { useEffect, useState } from 'react';
import styled from 'styled-components';
import { useDispatch, useSelector } from 'react-redux';

import { setLanguage, getLang } from 'store/auth/slice';

const LanguagePanel = styled.div`
  display: flex;
  flex-flow: row nowrap;
  margin: 0 10px 0 0;
`;

const LanguageItem = styled.div`
  display: flex;
  justify-content: center;
  align-content: center;
  text-align: center;
  font-family: inherit;
  font-size: 14px;
  font-style: normal;
  font-weight: 400;
  line-height: 19px;
  text-transform: uppercase;
  padding: 5px;
  margin: 18px 0 10px 0;
  cursor: pointer;
  border-radius: 50%;
  color: ${({ checked }) =>
    checked ? 'hsl(195, 100%, 53%)' : 'rgb(119, 119, 119)'};
  border: ${({ checked }) => (checked ? '1px solid #12b6ed;' : 'none')};

  &:hover {
    background-color: hsl(195, 100%, 90%);
    color: hsl(0, 0%, 10%);
  }
`;

const CustomSelect = () => {
  const dispatch = useDispatch();
  const lang = useSelector(getLang);
  const [languageArray, setLanguageArray] = useState([
    { value: 'ru', checked: false },
    { value: 'ua', checked: false },
    { value: 'en', checked: false },
  ]);

  useEffect(() => {
    const newLang = languageArray.map((item) =>
      item.value === lang
        ? { ...item, checked: true }
        : { ...item, checked: false }
    );
    setLanguageArray(newLang);
  }, [lang]);

  const handleSelected = (langValue) => {
    dispatch(setLanguage(langValue));
  };

  return (
    <LanguagePanel>
      {languageArray.map((option) => (
        <LanguageItem
          key={option.value}
          onClick={() => handleSelected(option.value)}
          checked={option.checked}
        >
          {option.value}
        </LanguageItem>
      ))}
    </LanguagePanel>
  );
};

export { CustomSelect };
