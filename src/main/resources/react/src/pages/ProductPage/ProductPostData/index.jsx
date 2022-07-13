import { useSelector } from 'react-redux';

import { getLang } from 'store/auth/slice';
import { Button } from 'components/common/buttons';
import { getTranslatedText } from 'components/local/localization';

import {
  TitleH2,
  Container,
  TitleContainer,
  ButtonContainer,
  PostDataDescription,
  PostDataBoxContainer,
  PostDataDescriptionOl,
  PostDataDescriptionUl,
  PostDataDescriptionText,
  PostDataDescriptionSpan,
  PostDataDescriptionOlItem,
  PostDataDescriptionUlItem,
} from './styles';

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
    <Container>
      <TitleContainer>
        <TitleH2>{title}</TitleH2>
      </TitleContainer>

      <PostDataDescription>
        <PostDataDescriptionText>
          <PostDataDescriptionSpan>
            {`${getTranslatedText('product.changesTo', lang)}:`}
          </PostDataDescriptionSpan>
        </PostDataDescriptionText>

        <PostDataDescriptionOl>
          {transformWishes.map((item, idx) => (
            <PostDataDescriptionOlItem key={String(`li_${idx}`)}>
              {item}
            </PostDataDescriptionOlItem>
          ))}
        </PostDataDescriptionOl>
      </PostDataDescription>

      <ButtonContainer>
        <Button
          text={getTranslatedText('product.button', lang)}
          width="250px"
        />
      </ButtonContainer>

      <TitleContainer>
        <TitleH2>
          {`${getTranslatedText('product.description', lang)}:`}
        </TitleH2>
      </TitleContainer>

      <PostDataBoxContainer>
        <PostDataDescription>
          <PostDataDescriptionUl>
            <PostDataDescriptionUlItem>
              <PostDataDescriptionSpan>
                {`${getTranslatedText(
                  'product.size',
                  lang
                )} / ${getTranslatedText('product.age', lang)}:`}
              </PostDataDescriptionSpan>
            </PostDataDescriptionUlItem>

            <PostDataDescriptionUlItem>
              <PostDataDescriptionSpan>
                {`${getTranslatedText('product.season', lang)}:`}
              </PostDataDescriptionSpan>
            </PostDataDescriptionUlItem>

            <PostDataDescriptionUlItem>
              <PostDataDescriptionSpan>
                {`${getTranslatedText('product.sex', lang)}:`}
              </PostDataDescriptionSpan>
            </PostDataDescriptionUlItem>
          </PostDataDescriptionUl>
        </PostDataDescription>

        <PostDataDescription>
          <PostDataDescriptionUl>
            <PostDataDescriptionUlItem>{`${size} / ${age}`}</PostDataDescriptionUlItem>

            <PostDataDescriptionUlItem>{season}</PostDataDescriptionUlItem>

            <PostDataDescriptionUlItem>{gender}</PostDataDescriptionUlItem>
          </PostDataDescriptionUl>
        </PostDataDescription>
      </PostDataBoxContainer>
    </Container>
  );
};
export default ProductPostData;
