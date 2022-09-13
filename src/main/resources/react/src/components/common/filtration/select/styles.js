import styled, { css } from 'styled-components';

export const TitleBlock = styled.div`
  display: flex;
  flex-direction: column;
  cursor: pointer;
`;

export const DisplayFlex = styled.div`
  display: flex;
  justify-content: space-between;
`;

export const Title = styled.span`
  font-size: 18px;
  line-height: 24px;
`;

export const OptionWrapper = styled.div`
  display: flex;
  flex-direction: column;
  gap: 4px;
  margin: 10px 0;
`;

export const SubTitleBlock = styled.div`
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 4px 8px 4px 58px;
  line-height: 18px;
  cursor: pointer;

  ${({ theme, isSelected }) => css`
    color: ${isSelected ? theme.colors.white : '#777777'};
    background-color: ${isSelected ? theme.colors.activeColor : 'transparent'};
    border-radius: 5px;

    ${!isSelected &&
    css`
      :hover {
        color: #2f2f2f;
        background-color: #b8e9fa;
      }
    `}
  `}
`;

export const Close = styled.div`
  display: flex;
  flex-shrink: 0;

  ${({ theme, isSelected }) => css`
    opacity: ${isSelected ? 1 : 0};

    svg {
      path {
        fill: ${theme.colors.white};
      }
    }
  `}
`;

export const RotateRectangle = styled.div`
  ${({ openSelect }) => css`
    ${openSelect &&
    css`
      svg {
        webkit-transform: rotate(180deg);
        transform: rotate(180deg);
      }
    `}
  `}
`;
