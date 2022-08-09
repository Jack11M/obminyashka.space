import styled, { css } from 'styled-components';

const ellipsisText = css`
  overflow: hidden;
  white-space: nowrap;
  text-overflow: ellipsis;
`;

export const EllipsisDiv = styled.div`
  ${ellipsisText};
`;

export const ToolTip = styled.p`
  position: absolute;
  padding: 16px;
  max-width: 500px;
  border-radius: 8px;
  overflow-x: auto;
  z-index: 1;
  box-shadow: 0px 0px 38px -24px rgba(0, 0, 0, 0.5),
    0px 31px 32px -24px rgba(0, 0, 0, 0.2);

  ${({ theme, height }) => css`
    background-color: ${theme.colors.white};
    color: ${theme.colors.blackColorText};
    top: ${height + 8}px;
  `}
`;
