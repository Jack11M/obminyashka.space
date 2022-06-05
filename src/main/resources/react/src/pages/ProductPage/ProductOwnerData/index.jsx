import { Avatar } from 'components/common/avatar';
import { getTranslatedText } from 'components/local/localization';

import './ProductOwnerData.scss';

const ProductOwnerData = ({ ava, name, date, city, phone }) => (
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
            <span>{`${getTranslatedText('product.dateOfAdv')}:`}</span>
          </li>
          <li>
            <span>{`${getTranslatedText('product.cityOfAdv')}:`}</span>
          </li>
          <li>
            <span>{`${getTranslatedText('product.phoneOfAdv')}:`}</span>
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

export default ProductOwnerData;
