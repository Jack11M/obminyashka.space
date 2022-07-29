import styled, { css } from 'styled-components';

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

export const NoPhotoImg = styled.img`
  width: 40%;
  height: 40%;
  margin: 30% 30%;
  object-fit: contain;
`;
