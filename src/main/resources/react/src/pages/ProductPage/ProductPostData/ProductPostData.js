import React from 'react';

import Button from '../../../components/button/Button';

import './ProductPostData.scss';
import { useSelector } from 'react-redux';
import { getTranslatedText } from '../../../components/local/localisation';

const ProductPostData = ({ title, wishes, gender, age, season, size }) => {

  const { lang } = useSelector( state => state.auth );

  return (
    <div className = 'productPostData'>
      <div className = 'postHeading'>
        <h2>{ title }</h2>
      </div>
      <div className = 'postDataDescription'>
        <p><span>{getTranslatedText('product.changesTo', lang)}:</span></p>
        <ol>
          {
            wishes.map((item, idx) => <li key = { `li_${ idx }` }> { item } </li>
            ) }
        </ol>
      </div>
      <div className = 'postButton'>
        <Button
          text = {getTranslatedText('product.button', lang)}
          width = { '250px' }
        />
      </div>
      <div className = 'postHeading'>
        <h2>{getTranslatedText('product.description', lang)}:</h2>
      </div>
      <div className = 'postDataBox'>
        <div className = 'postDataDescription'>
          <ul>
            <li><span>{getTranslatedText('product.size', lang)} / {getTranslatedText('product.age', lang)}:</span></li>
            <li><span>{getTranslatedText('product.season', lang)}:</span></li>
            <li><span>{getTranslatedText('product.sex', lang)}:</span></li>
          </ul>
        </div>
        <div className = 'postDataDescription'>
          <ul>
            <li>{ size } / { age }</li>
            <li>{ season }</li>
            <li>{ gender }</li>
          </ul>
        </div>
      </div>
    </div>
  );
};
export default ProductPostData;
