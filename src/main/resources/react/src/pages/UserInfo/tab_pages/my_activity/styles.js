import styled from 'styled-components';

export const Container = styled.div`
  > .incoming__replies-text {
    margin: 65px 0 40px;
  }
  > .outgoing__replies-text {
    margin: 83px 0 40px;
  }
`;

export const CardBlock = styled.div`
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  row-gap: 28px;
  column-gap: 20px;
`;

export const StyledCardBlock = styled(CardBlock)`
  margin-bottom: 100px;
`;
