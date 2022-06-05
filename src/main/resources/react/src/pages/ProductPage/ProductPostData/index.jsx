import { Button } from 'components/common/buttons';
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
  const transformWishes = readyForOffers
    ? [wishes, getTranslatedText('product.checkInUl')]
    : wishes;

  return (
    <div className="productPostData">
      <div className="postHeading">
        <h2>{title}</h2>
      </div>
      <div className="postDataDescription">
        <p>
          <span>{`${getTranslatedText('product.changesTo')}:`}</span>
        </p>
        <ol>
          {transformWishes.map((item, idx) => (
            <li key={String(`li_${idx}`)}>{item}</li>
          ))}
        </ol>
      </div>
      <div className="postButton">
        <Button text={getTranslatedText('product.button')} width="250px" />
      </div>
      <div className="postHeading">
        <h2>{`${getTranslatedText('product.description')}:`}</h2>
      </div>
      <div className="postDataBox">
        <div className="postDataDescription">
          <ul>
            <li>
              <span>
                {`${getTranslatedText('product.size')} / ${getTranslatedText(
                  'product.age'
                )}:`}
              </span>
            </li>
            <li>
              <span>{`${getTranslatedText('product.season')}:`}</span>
            </li>
            <li>
              <span>{`${getTranslatedText('product.sex')}:`}</span>
            </li>
          </ul>
        </div>
        <div className="postDataDescription">
          <ul>
            <li>{`${size} / ${age}`}</li>

            <li>{season}</li>
            <li>{gender}</li>
          </ul>
        </div>
      </div>
    </div>
  );
};
export default ProductPostData;
