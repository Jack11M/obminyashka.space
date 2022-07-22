import {
  Title,
  Container,
  TitleContainer,
  DescriptionText,
  DescriptionContainer,
} from './styles';

const ProductDescription = ({ title, description }) => (
  <Container>
    <TitleContainer>
      <Title>{title}</Title>
    </TitleContainer>

    <DescriptionContainer>
      <DescriptionText>{description}</DescriptionText>
    </DescriptionContainer>
  </Container>
);
export default ProductDescription;
