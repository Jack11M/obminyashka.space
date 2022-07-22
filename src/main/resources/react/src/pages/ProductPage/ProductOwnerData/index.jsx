import { Avatar } from 'components/common/avatar';
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
            <PostReqDataUlItem>
              <PostReqDataSpan>
                {`${getTranslatedText('product.dateOfAdv')}:`}
              </PostReqDataSpan>
            </PostReqDataUlItem>

            <PostReqDataUlItem>
              <PostReqDataSpan>
                {`${getTranslatedText('product.cityOfAdv')}:`}
              </PostReqDataSpan>
            </PostReqDataUlItem>

            <PostReqDataUlItem>
              <PostReqDataSpan>
                {`${getTranslatedText('product.phoneOfAdv')}:`}
              </PostReqDataSpan>
            </PostReqDataUlItem>
          </PostReqDataUl>
        </PostReqData>

        <PostResData>
          <PostReqDataUl>
            <PostReqDataUlItem>{date}</PostReqDataUlItem>

            <PostReqDataUlItem>{city}</PostReqDataUlItem>

            <PostReqDataUlItem>{phone}</PostReqDataUlItem>
          </PostReqDataUl>
        </PostResData>
      </PostData>
    </Container>
  );
};
export { ProductOwnerData };
