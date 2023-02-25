import styled, { css } from 'styled-components';
import { Icon } from '@wolshebnik/obminyashka-components';

export const Header = styled.div`
  padding: 8px 16px;
  display: flex;
  justify-content: space-between;
  align-items: center;
`;

export const DateContainer = styled.div`
  display: flex;
  gap: 16px;
`;

const commonChevron = css`
  transition: all 0.3s;
  cursor: pointer;

  path {
    fill: #12b6ed;
  }

  :hover {
    transform: scale(1.2);
  }

  :active {
    transform: scale(1.4);
  }

  ${({ theme, disabled }) => css`
    ${disabled &&
    css`
      cursor: default;
      pointer-events: none;

      path {
        fill: ${theme.colors['btn-gb-disabled']};
      }
    `}
  `}
`;

export const ChevronLeft = styled(Icon.ChevronLeft)`
  ${commonChevron};
`;
export const ChevronRight = styled(Icon.ChevronRight)`
  ${commonChevron};
`;
