import styled, { css } from 'styled-components';

export const TitleBlock = styled.div`
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  cursor: pointer;
`;

export const TitleBlockWrapper = styled.div`
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
  gap: 5px;
  margin: 20px 0 10px;

  ${({ hideSelect }) => css`
    display: ${hideSelect ? 'flex' : 'none'};
  `}
`;

export const Close = styled.div`
  display: flex;
  flex-shrink: 0;
  transition: transform 0.3s linear;
  -webkit-transition: transform 0.3s linear;

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

export const SubTitleBlock = styled.div`
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-right: 15px;
  padding: 5px 15px;
  line-height: 17px;
  cursor: pointer;

  ${({ theme, isSelected }) => css`
    color: ${isSelected ? theme.colors.white : '#777777'};
    background-color: ${isSelected ? theme.colors.activeColor : 'transparent'};
    border-radius: 5px;

    :hover {
      ${Close} {
        transform: rotate(90deg);
        -webkit-transform: rotate(90deg);
      }
    }

    ${!isSelected &&
    css`
      :hover {
        color: #2f2f2f;
        background-color: #b8e9fa;
    `}
  `}
`;
