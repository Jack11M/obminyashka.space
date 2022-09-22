import { normalize } from 'styled-normalize';
import { createGlobalStyle, css } from 'styled-components';

export const GlobalStyles = createGlobalStyle`
  ${normalize};

  * {
    box-sizing: border-box;
    padding: 0;
    margin: 0;
    font-family: Roboto, sans-serif;
  }

  body {
    line-height: 1;
    font-size: 16px;
    font-weight: 400;
  }

  button {
    border: none;
    background-color: inherit;
    color: inherit;
    line-height: inherit;
    cursor: pointer;
  }

  input {
    font-weight: 400;
    font-size: 16px;
    line-height: 1px;
    color: inherit;
  }

  a {
  text-decoration: none;
  color: inherit;
  }

  li {
  list-style-type: none;
}

 ${({ theme }) => css`
   /* Track */
   ::-webkit-scrollbar-track {
     background: ${theme.colors.scrollbarBg};
   }

   /* Handle */
   ::-webkit-scrollbar-thumb {
     background: ${theme.colors.btnBlue};
     border-radius: 10px;
   }

   /* Handle on hover */
   ::-webkit-scrollbar-thumb:hover {
     background: ${theme.colors.btnBlueActive};
   }
 `}

  ::-webkit-scrollbar {
  width: 10px;
  height: 10px;
  }



`;
