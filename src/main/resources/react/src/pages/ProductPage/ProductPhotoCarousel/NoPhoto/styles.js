import styled, { css } from 'styled-components';
import { Icon } from '@wolshebnik/obminyashka-components';

export const NoPhotoContainer = styled.div`
  margin: 10px 0;
  width: 138px;
  height: 138px;

  ${({ noPhoto, theme }) => css`
    background-color: ${theme.colors.white};

    ${noPhoto &&
    css`
      display: flex;
      align-items: center;
      width: 600px;
      height: 680px;
      margin: 0;
    `}
  `}
`;

export const NoPhotoImg = styled(Icon.NoPhoto)`
  width: 50%;
  height: 41%;
  margin: 0 auto;
  object-fit: contain;
`;
