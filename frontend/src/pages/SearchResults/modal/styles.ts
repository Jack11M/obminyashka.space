/* eslint-disable no-empty-pattern */
import styled, { css } from "styled-components";

export const ModalWrapper = styled.div<{ isModal: boolean }>`
  position: fixed;
  top: 0;
  left: 0;
  height: 100%;
  width: 100%;
  background: rgba(115, 181, 206, 0.4);
  opacity: 0;
  pointer-events: none;
  transition: 0.5s;
  z-index: 100;
  overflow: auto;

  ${({ isModal }) => css`
    ${isModal &&
    css`
      opacity: 1;
      pointer-events: all;
    `}
  `};
`;

export const ModalContent = styled.div<{ isModal: boolean }>`
  display: flex;
  justify-content: center;
  padding: 150px 35px;
  height: auto;
  width: fit-content;
  background-color: white;
  transform: translateX(-100%);
  transition: 0.5s all;

  ${({ theme, isModal }) => css`
    ${isModal &&
    css`
      opacity: 1;
      pointer-events: all;
      transform: translateX(0);

      ${theme.responsive.isMobile &&
      css`
        width: 100%;
      `}

      ${theme.responsive.isMobileBG &&
      css`
        padding: 150px 40px;
        width: fit-content;
      `}
    `}
  `}
`;

export const Cross = styled.div`
  position: relative;

  ${({ theme }) => css`
    &:before,
    &:after {
      content: "";
      position: absolute;
      top: -40px;
      right: -30px;
      width: 30px;
      height: 1.5px;
      background: ${theme.colors.categoryFilter.border};

      ${theme.responsive.isMobileBG &&
      css`
        right: -32px;
      `}
    }

    &:before {
      webkit-transform: rotate(45deg);
      transform: rotate(45deg);
    }

    &:after {
      webkit-transform: rotate(-45deg);
      transform: rotate(-45deg);
    }
  `}
`;
