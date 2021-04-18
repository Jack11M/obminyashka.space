import React, { useEffect, useRef, useState } from 'react';
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
  background-color: ${ props => props.checked ? 'hsl(195, 100%, 53%)' : '#F2F4F7' };
  color: rgb(119, 119, 119);
  
  &:hover {
    background-color: hsl(195, 100%, 90%);
    color: hsl(0, 0%, 10%);;
  }
`;

const languageArray = [
  { value : 'ru', checked : false },
  { value : 'ua', checked : false },
  { value : 'en', checked : false }
];

const CustomSelect = () => {
  const dispatch = useDispatch();
  const { lang : language } = useSelector(state => state.auth);

  const selectedOption = languageArray.map(lang =>
    lang.value === language
      ?
      { ...lang, checked : true }
      :
      { ...lang, checked : false }
  );

  useEffect(() => {
    document.body.addEventListener('click', selectedOption);
  }, []);

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
