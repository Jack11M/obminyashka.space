import styled, { css } from 'styled-components';

export const TitleBlock = styled.div`
  display: flex;
  justify-content: space-between;
  align-items: center;
  cursor: pointer;
`;

export const Title = styled.span`
  font-size: 18px;
  line-height: 24px;
`;

export const SubTitleBlock = styled.div`
  display: flex;
  flex-direction: column;
  gap: 15px;
  margin: 20px 0 10px;
  line-height: 17px;
`;

export const RotateRectangle = styled.div`
  padding-left: 10px;

  svg {
    transition: all 0.2s linear;
    -webkit-transition: all 0.2s linear;
  }

  ${({ openSelect }) => css`
    ${openSelect &&
    css`
      svg {
        transform: rotate(180deg);
        webkit-transform: rotate(180deg);
      }
    `}
  `}
`;
