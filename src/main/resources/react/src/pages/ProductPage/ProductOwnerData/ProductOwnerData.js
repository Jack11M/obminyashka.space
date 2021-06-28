import React from 'react';
import { useSelector } from 'react-redux';

import Avatar from '../../../components/common/avatar/avatar';

import './ProductOwnerData.scss';
import { getTranslatedText } from '../../../components/local/localisation';

const ProductOwnerData = ({ ava, name, date, city, phone }) => {
  const { lang } = useSelector((state) => state.auth);

  return (
    <div className="productOwnerDataBox">
      <div className="productOwner">
        <div className="productOwnerAvatar">
          <Avatar height={30} width={28} avatar={ava} />
        </div>
        <div className="productOwnerName">
          <h2>{name}</h2>
        </div>
      </div>
      <div className="postData">
        <div className="postReqData">
          <ul>
            <li>
              <span>{getTranslatedText('product.dateOfAdv', lang)}:</span>
            </li>
            <li>
              <span>{getTranslatedText('product.cityOfAdv', lang)}:</span>
            </li>
            <li>
              <span>{getTranslatedText('product.phoneOfAdv', lang)}:</span>
            </li>
          </ul>
        </div>
        <div className="postResData">
          <ul>
            <li>{date}</li>
            <li>{city}</li>
            <li>{phone}</li>
          </ul>
        </div>
      </div>
    </div>
  );
};
export default ProductOwnerData;
