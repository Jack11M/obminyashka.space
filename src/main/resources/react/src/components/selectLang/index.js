import React, { useState, useEffect } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import styled from 'styled-components';
import { chooseLanguage, clearValueLogin, clearValueSignUp } from '../../redux/auth/action';
import { changeLangProfileErrors } from '../../redux/profile/profileAction';

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
  color: ${ ({ checked }) => checked ? 'hsl(195, 100%, 53%)' : 'rgb(119, 119, 119)' };

  &:hover {
    background-color: hsl(195, 100%, 90%);
    color: hsl(0, 0%, 10%);;
  }
`;

const CustomSelect = () => {
  const dispatch = useDispatch();
  const { lang : language } = useSelector(state => state.auth);
  const [ languageArray, setLanguageArray ] = useState([
    { value : 'ru', checked : false },
    { value : 'ua', checked : false },
    { value : 'en', checked : false }
  ]);

  useEffect(() => {
    const checkedArray = languageArray.map(item => item.value === language
      ?
      { ...item, checked : true }
      :
      { ...item, checked : false }
    );
    setLanguageArray(checkedArray);
  }, [ language ]);

  const handleSelected = (lang) => {
    dispatch(clearValueSignUp());
    dispatch(clearValueLogin());
    dispatch(changeLangProfileErrors(lang));
    dispatch(chooseLanguage(lang));
  };

  return (
    <LanguagePanel>
      {
        languageArray.map(option =>
          <LanguageItem
            key = { option.value }
            onClick = { () => handleSelected(option.value) }
            checked = { option.checked }
          >
            { option.value }
          </LanguageItem>)
      }
    </LanguagePanel>
  );
};

export default CustomSelect;
