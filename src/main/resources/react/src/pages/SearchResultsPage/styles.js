import styled from 'styled-components';

export const SearchingResults = styled.div`
  padding: 0 15px;
  margin: 0 auto;
  margin-top: 200px;
  width: 100%;
  max-width: 1251px;
  overflow: hidden;
  margin-bottom: 150px;
`;

export const SearchingContent = styled.div`
  display: flex;
  flex-direction: row;
  margin-top: 10px;
`;

export const CardsContainer = styled.div`
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  row-gap: 8px;
  column-gap: 0;
`;

export const FilterContainer = styled.div`
  margin-right: 60px;
`;

export const BreadCrumbs = styled.div`
  margin-bottom: 30px;
  color: #8f8f8f;
  font-size: 16px;
  line-height: 26px;
  font-family: Roboto;
`;

export const Span = styled.span`
  color: #11171f;
`;
