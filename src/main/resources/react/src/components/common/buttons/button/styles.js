import styled, { css } from 'styled-components';

export const WrapIcon = styled.span`
  line-height: 0;
  margin-left: 8px;
  order: 0;
`;

export const ButtonBlue = styled.button`
  display: flex;
  justify-content: center;
  align-items: center;
  border-radius: 25px;
  border: 0;
  outline: none;
  height: 49px;
  color: #fdf9ff;
  text-transform: uppercase;
  transition: background-color 0.3s ease;

  ${({ theme: { colors }, width, bold, lHeight, mb }) => css`
    width: ${width};
    background-color: ${colors['btn-blue-normal']};
    font-weight: ${bold ? 'bold' : 'normal'};
    line-height: ${lHeight || '20px'};

    ${mb &&
    css`
      margin-bottom: ${mb};
    `}

    &:hover {
      cursor: pointer;
      background-color: ${colors['btn-blue-hover']};
      > span > svg {
        path {
          transition: all 0.3s ease;
          fill: white;
        }
      }
    }

    &:active {
      cursor: pointer;
      background-color: ${colors['btn-blue-active']};
    }

    &:disabled {
      background-color: ${colors['btn-gb-disabled']};
    }
  `}
`;
