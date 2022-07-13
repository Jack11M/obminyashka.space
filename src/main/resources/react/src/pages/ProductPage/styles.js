import styled from 'styled-components';

export const TopSection = styled.section`
  margin-top: 150px;
`;

export const BottomSection = styled.section`
  margin: auto;
  max-width: 1220px;
  padding-top: 100px;
`;

export const ProductPageContainer = styled.div`
  margin: auto;
  max-width: 1220px;
  padding-top: 50px;
`;

export const BreadCrumbs = styled.div`
  font-family: 'Roboto', sans-serif;
  font-size: 16px;
  line-height: 26px;
  color: #8f8f8f;
  margin-bottom: 30px;
`;

export const Span = styled.span`
  color: #11171f;
`;

export const ProductPageInner = styled.div`
  font-family: 'Roboto', sans-serif;
  display: flex;
  flex-wrap: wrap;
  flex-direction: row;
  justify-content: space-between;
`;

export const CarouselAndDescription = styled.div`
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  width: 765px;
  height: 935px;

  .noCarouselImg {
    height: 140px;
    width: 140px;
    margin: 10px 0;
    object-fit: contain;
  }

  .bigNoPhoto {
    width: 600px;
    height: 680px;
    background-color: #ffffff;

    .bigNoPhotoImg {
      width: 40%;
      height: 40%;
      margin: 30% 30%;
    }
  }
`;

export const OwnerAndPost = styled.div`
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  width: 420px;
  height: 935px;
`;

export const SectionHeading = styled.div`
  display: flex;
  flex-direction: row;
  align-items: center;
`;
