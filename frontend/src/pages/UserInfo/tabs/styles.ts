import styled, { css } from 'styled-components';
import { NavLink as Link } from 'react-router-dom';

export const TabsBlock = styled.div`
  display: flex;
  flex-direction: column;
  gap: 24px;
  margin-top: 26px;
  margin-right: 64px;
`;

export const Circle = styled.span`
  position: absolute;

  width: 50px;
  height: 30px;
  opacity: 0;
  top: -8px;
  right: -95px;
  border-radius: 50%;

  ${({ theme }) => css`
    background-color: ${theme.colors.bgContent};

    &:after {
      position: absolute;
      content: '';
      width: 12px;
      height: 12px;
      border-radius: 50%;
      left: 4px;
      top: calc(50% - 6px);
      background-color: ${theme.colors.btnBlue};
    }
  `}
`;

export const NavLink = styled(Link)`
  position: relative;
  display: flex;
  align-items: center;
  gap: 16px;
  font-size: 14px;
  line-height: 16px;
  white-space: nowrap;
  transition: 0.3s;

  &:not(.active):hover {
    transform: scale(1.05);
  }

  &.active {
    transform: translateX(10px);
    color: ${({ theme }) => theme.colors.btnBlue};
    transition: 0.5s;

    ${Circle} {
      opacity: 1;
    }

    > svg path {
      fill: ${({ theme }) => theme.colors.btnBlue};
    }
  }
`;
