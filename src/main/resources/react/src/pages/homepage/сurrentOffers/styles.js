import styled from 'styled-components';

export const ProductSection = styled.section`
  margin-top: 90px;
`;

export const ProductHeader = styled.div`
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 50px;
`;

export const ProductListUl = styled.ul`
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  grid-gap: 5px;
`;
