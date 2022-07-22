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
  height: 49px;
  color: #fdf9ff;
  font-family: 'Roboto', sans-serif;
  font-style: normal;
  font-size: 16px;
  text-transform: uppercase;
  border: 0;
  border-radius: 25px;
  transition: background-color 0.3s ease;
  cursor: pointer;
  outline: none;

  ${({ width, bold, mb, theme, lHeight }) => css`
    width: ${width};
    font-weight: ${bold ? 'bold' : 'normal'};
    background-color: ${theme.colors.btnBlue};
    line-height: ${lHeight || '20px'};

    ${mb &&
    css`
      margin-bottom: ${mb};
    `}

    &:hover {
      cursor: pointer;
      background-color: ${theme.colors.btnBlueHover};
      > span > svg {
        path {
          transition: all 0.3s ease;
          fill: white;
        }
      }
    }

    &:active {
      background-color: ${theme.colors.btnBlueActive};
    }

    &:disabled {
      background-color: ${theme.colors.btnGbDisabled};
    }
  `}
`;
