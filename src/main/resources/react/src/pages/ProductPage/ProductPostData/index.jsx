import { useMemo } from 'react';
import { Icon } from '@wolshebnik/obminyashka-components';

import { getStorageLang } from 'Utils';
import { Button } from 'components/common';
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
  const lang = getStorageLang();
  const transformWishes = readyForOffers
    ? [...wishes, getTranslatedText('product.checkInUl')]
    : wishes;

  const sm = useMemo(() => (lang === 'en' ? 'sm' : 'см'), [lang]);

  return (
    <Styles.Container>
      <div>
        <Styles.TitleContainer>
          <Styles.TitleH2>{title}</Styles.TitleH2>
        </Styles.TitleContainer>

        <Styles.PostDataDescription>
          <Styles.PostDataDescriptionText>
            <Styles.PostDataDescriptionSpan>
              {`${getTranslatedText('product.changesTo')}:`}
            </Styles.PostDataDescriptionSpan>
          </Styles.PostDataDescriptionText>

          <Styles.PostDataDescriptionOl>
            {transformWishes?.map((item, idx) => (
              <Styles.PostDataDescriptionOlItem key={String(`li_${idx}`)}>
                <Icon.ProductPostCheck style={{ marginRight: '4px' }} />
                {item}
              </Styles.PostDataDescriptionOlItem>
            ))}
          </Styles.PostDataDescriptionOl>
        </Styles.PostDataDescription>

        <Styles.ButtonContainer>
          <Button text={getTranslatedText('product.button')} width="250px" />
        </Styles.ButtonContainer>
      </div>

      <div>
        <Styles.TitleContainer>
          <Styles.TitleH2>
            {`${getTranslatedText('product.description')}:`}
          </Styles.TitleH2>
        </Styles.TitleContainer>

        <Styles.PostDataBoxContainer>
          <Styles.PostDataDescription>
            <Styles.PostDataDescriptionUl>
              <Styles.PostDataDescriptionUlItem>
                <Styles.PostDataDescriptionSpan>
                  {`${
                    size ? `${getTranslatedText('product.size')} /` : ''
                  } ${getTranslatedText('product.age')}:`}
                </Styles.PostDataDescriptionSpan>
              </Styles.PostDataDescriptionUlItem>

              <Styles.PostDataDescriptionUlItem>
                <Styles.PostDataDescriptionSpan>
                  {`${getTranslatedText('product.season')}:`}
                </Styles.PostDataDescriptionSpan>
              </Styles.PostDataDescriptionUlItem>

              <Styles.PostDataDescriptionUlItem>
                <Styles.PostDataDescriptionSpan>
                  {`${getTranslatedText('product.sex')}:`}
                </Styles.PostDataDescriptionSpan>
              </Styles.PostDataDescriptionUlItem>
            </Styles.PostDataDescriptionUl>
          </Styles.PostDataDescription>

          <Styles.PostDataDescription>
            <Styles.PostDataDescriptionUl>
              <Styles.PostDataDescriptionUlItem>
                {`${size ? `${size} ${sm} /` : ''} ${age ?? ''}`}
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
      </div>
    </Styles.Container>
  );
};
export { ProductPostData };
