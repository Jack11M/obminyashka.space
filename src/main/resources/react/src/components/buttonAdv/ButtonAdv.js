import React from "react";
import { useSelector } from 'react-redux';
import { getTranslatedText } from '../local/localisation';
import {ButtonAdd, Span} from './style_button_adv';

const ButtonAdv = () => {
  const { lang } =useSelector(state => state.auth)
  return (
    <ButtonAdd to={'/AddGoods'} >
      <Span>{ getTranslatedText('button.addAdv', lang) }</Span>
    </ButtonAdd>
  );
};

export default ButtonAdv;
