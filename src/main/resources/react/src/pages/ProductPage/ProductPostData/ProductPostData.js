import React from 'react';
import { useSelector } from 'react-redux';

import { getLang } from 'store/auth/slice';
import Button from 'components/common/buttons/button/Button';
import { getTranslatedText } from 'components/local/localization';

import './ProductPostData.scss';

const ProductPostData = ({
  age,
  size,
  title,
  wishes,
  gender,
  season,
  readyForOffers,
}) => {
  const lang = useSelector(getLang);

  const transformWishes = readyForOffers
    ? [wishes, getTranslatedText('product.checkInUl', lang)]
    : wishes;

  return (
    <div className="productPostData">
      <div className="postHeading">
        <h2>{title}</h2>
      </div>
      <div className="postDataDescription">
        <p>
          <span>{getTranslatedText('product.changesTo', lang)}:</span>
        </p>
        <ol>
          {transformWishes.map((item, idx) => (
            <li key={`li_${idx}`}> {item} </li>
          ))}
        </ol>
      </div>
      <div className="postButton">
        <Button
          text={getTranslatedText('product.button', lang)}
          width={'250px'}
        />
      </div>
      <div className="postHeading">
        <h2>{getTranslatedText('product.description', lang)}:</h2>
      </div>
      <div className="postDataBox">
        <div className="postDataDescription">
          <ul>
            <li>
              <span>
                {getTranslatedText('product.size', lang)} /{' '}
                {getTranslatedText('product.age', lang)}:
              </span>
            </li>
            <li>
              <span>{getTranslatedText('product.season', lang)}:</span>
            </li>
            <li>
              <span>{getTranslatedText('product.sex', lang)}:</span>
            </li>
          </ul>
        </div>
        <div className="postDataDescription">
          <ul>
            <li>
              {size} / {age}
            </li>
            <li>{season}</li>
            <li>{gender}</li>
          </ul>
        </div>
      </div>
    </div>
  );
};
export default ProductPostData;
