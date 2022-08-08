import styled, { css } from 'styled-components';

export const AvatarBlock = styled.div`
  display: flex;
  justify-content: center;
  align-items: center;
  width: 100%;
`;

export const Image = styled.img`
  border-radius: 50%;
  object-fit: cover;

  ${({ width, height, isLoading }) => css`
    display: ${isLoading ? 'none' : 'block'};
    width: ${width}px;
    height: ${height}px;
  `}
`;
