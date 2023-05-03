import styled, { css } from 'styled-components';

export const CardsContainerStyles = css`
  display: flex;
  flex-direction: row;
  flex-wrap: wrap;
  gap: 20px;
  width: 960px;
`;

export const InputResponseContainer = styled.div`
  ${CardsContainerStyles};
`;

export const OutputResponseContainer = styled.div`
  ${CardsContainerStyles};
  margin-bottom: 100px;
`;
