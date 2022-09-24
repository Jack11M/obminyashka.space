import styled from 'styled-components';

export const CardsContainer = styled.div`
  display: flex;
  flex-direction: row;
  flex-wrap: wrap;
  gap: 20px;
  width: 960px;
`;

export const StylizedCardBlock = styled(CardsContainer)`
  margin-bottom: 100px;
`;
