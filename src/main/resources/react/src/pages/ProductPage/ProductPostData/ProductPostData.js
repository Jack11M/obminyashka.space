import React from 'react';

import Button from '../../../components/button/Button';

import './ProductPostData.scss';

const ProductPostData = ({ title, wishes, gender, age, season, size }) => {

  return (
    <div className = 'productPostData'>
      <div className = 'postHeading'>
        <h2>{ title }</h2>
      </div>
      <div className = 'postDataDescription'>
        <p><span>Меняет на:</span></p>
        <ol>
          {
            wishes.map((item, idx) => <li key = { `li_${ idx }` }> { item } </li>
            ) }
        </ol>
      </div>
      <div className = 'postButton'>
        <Button
          text = "Предложить обмен"
          width = { '250px' }
        />
      </div>
      <div className = 'postHeading'>
        <h2>Описание:</h2>
      </div>
      <div className = 'postDataBox'>
        <div className = 'postDataDescription'>
          <ul>
            <li><span>Размер/возраст:</span></li>
            <li><span>Сезон:</span></li>
            <li><span>Пол:</span></li>
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
