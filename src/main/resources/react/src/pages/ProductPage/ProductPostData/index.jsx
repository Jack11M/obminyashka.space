import { useSelector } from 'react-redux';

import { getLang } from 'store/auth/slice';
import { Button } from 'components/common/buttons';
import { getTranslatedText } from 'components/local/localization';

import * as Styles from './styles';

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
    <Styles.Container>
      <Styles.TitleContainer>
        <Styles.TitleH2>{title}</Styles.TitleH2>
      </Styles.TitleContainer>

      <Styles.PostDataDescription>
        <Styles.PostDataDescriptionText>
          <Styles.PostDataDescriptionSpan>
            {`${getTranslatedText('product.changesTo', lang)}:`}
          </Styles.PostDataDescriptionSpan>
        </Styles.PostDataDescriptionText>

        <Styles.PostDataDescriptionOl>
          {transformWishes?.map((item, idx) => (
            <Styles.PostDataDescriptionOlItem key={String(`li_${idx}`)}>
              {item}
            </Styles.PostDataDescriptionOlItem>
          ))}
        </Styles.PostDataDescriptionOl>
      </Styles.PostDataDescription>

      <Styles.ButtonContainer>
        <Button
          text={getTranslatedText('product.button', lang)}
          width="250px"
        />
      </Styles.ButtonContainer>

      <Styles.TitleContainer>
        <Styles.TitleH2>
          {`${getTranslatedText('product.description', lang)}:`}
        </Styles.TitleH2>
      </Styles.TitleContainer>

      <Styles.PostDataBoxContainer>
        <Styles.PostDataDescription>
          <Styles.PostDataDescriptionUl>
            <Styles.PostDataDescriptionUlItem>
              <Styles.PostDataDescriptionSpan>
                {`${getTranslatedText(
                  'product.size',
                  lang
                )} / ${getTranslatedText('product.age', lang)}:`}
              </Styles.PostDataDescriptionSpan>
            </Styles.PostDataDescriptionUlItem>

            <Styles.PostDataDescriptionUlItem>
              <Styles.PostDataDescriptionSpan>
                {`${getTranslatedText('product.season', lang)}:`}
              </Styles.PostDataDescriptionSpan>
            </Styles.PostDataDescriptionUlItem>

            <Styles.PostDataDescriptionUlItem>
              <Styles.PostDataDescriptionSpan>
                {`${getTranslatedText('product.sex', lang)}:`}
              </Styles.PostDataDescriptionSpan>
            </Styles.PostDataDescriptionUlItem>
          </Styles.PostDataDescriptionUl>
        </Styles.PostDataDescription>

        <Styles.PostDataDescription>
          <Styles.PostDataDescriptionUl>
            <Styles.PostDataDescriptionUlItem>
              {`${size} / ${age}`}
            </Styles.PostDataDescriptionUlItem>

            <Styles.PostDataDescriptionUlItem>
              {season}
            </Styles.PostDataDescriptionUlItem>

            <Styles.PostDataDescriptionUlItem>
              {gender}
            </Styles.PostDataDescriptionUlItem>
          </Styles.PostDataDescriptionUl>
        </Styles.PostDataDescription>
      </Styles.PostDataBoxContainer>
    </Styles.Container>
  );
};
export default ProductPostData;
