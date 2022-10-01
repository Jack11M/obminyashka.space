import styled, { css } from 'styled-components';

export const EllipsisDiv = styled.div`
  overflow: hidden;
  white-space: nowrap;
  text-overflow: ellipsis;
`;

export const ToolTip = styled.p`
  position: absolute;
  padding: 16px;
  max-width: 500px;
  border-radius: 8px;
  overflow-x: auto;
  z-index: 10;
  cursor: context-menu;
  box-shadow: 10px 10px 20px rgba(0, 0, 0, 0.3),
    0px 31px 32px -24px rgba(0, 0, 0, 1);

  ${({ theme, height }) => css`
    background-color: ${theme.colors.white};
    color: ${theme.colors.blackColorText};
    top: ${height + 8}px;
  `}
`;
