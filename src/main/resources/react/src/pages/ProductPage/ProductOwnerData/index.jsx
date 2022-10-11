import { Avatar, EllipsisText } from 'components/common';
import { getTranslatedText } from 'components/local/localization';

import {
  PostData,
  Container,
  PostReqData,
  PostResData,
  ProductOwner,
  PostReqDataUl,
  PostReqDataSpan,
  ProductOwnerName,
  PostReqDataUlItem,
  ProductOwnerAvatar,
  ProductOwnerTitleH2,
} from './styles';

const ProductOwnerData = ({ avatar, name, date, city, phone }) => {
  return (
    <Container>
      <ProductOwner>
        <ProductOwnerAvatar>
          <Avatar height={32} width={32} avatar={avatar} />
        </ProductOwnerAvatar>

        <ProductOwnerName>
          <ProductOwnerTitleH2>{name}</ProductOwnerTitleH2>
        </ProductOwnerName>
      </ProductOwner>

      <PostData>
        <PostReqData>
          <PostReqDataUl>
            {date && (
              <PostReqDataUlItem>
                <PostReqDataSpan>
                  {`${getTranslatedText('product.dateOfAdv')}:`}
                </PostReqDataSpan>
              </PostReqDataUlItem>
            )}

            <PostReqDataUlItem>
              <PostReqDataSpan>
                {`${getTranslatedText('product.cityOfAdv')}:`}
              </PostReqDataSpan>
            </PostReqDataUlItem>

            {phone && (
              <PostReqDataUlItem>
                <PostReqDataSpan>
                  {`${getTranslatedText('product.phoneOfAdv')}:`}
                </PostReqDataSpan>
              </PostReqDataUlItem>
            )}
          </PostReqDataUl>
        </PostReqData>

        <PostResData>
          <PostReqDataUl>
            {date && <PostReqDataUlItem>{date}</PostReqDataUlItem>}

            <PostReqDataUlItem style={{ width: '200px' }}>
              <EllipsisText>{city}</EllipsisText>
            </PostReqDataUlItem>

            {phone && <PostReqDataUlItem>{phone}</PostReqDataUlItem>}
          </PostReqDataUl>
        </PostResData>
      </PostData>
    </Container>
  );
};
export { ProductOwnerData };
