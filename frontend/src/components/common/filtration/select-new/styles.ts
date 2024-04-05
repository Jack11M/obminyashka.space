/* eslint-disable no-empty-pattern */
import styled, { css, keyframes } from "styled-components";

const rotate = keyframes`
  0% {
    transform: rotate(0deg);
  }
  100% {
    transform: rotate(360deg);
  }
  `;

export const Wrapper = styled.div<{ filtration?: boolean; isOpen: boolean }>`
  position: relative;
  width: 100%;

  ${({ filtration, isOpen, theme }) => css`
    ${filtration &&
    css`
      padding: 0 10px;
      margin-right: 16px;
      border-radius: 5px;
      border: 2px dashed ${theme.colors.categoryFilter.locationBorder};
    `}

    ${isOpen &&
    filtration &&
    css`
      padding: 0 15px 20px;
    `}
  `}
`;

export const TitleContainer = styled.div`
  display: flex;
  justify-content: space-between;
  align-items: center;
`;

export const Title = styled.input<{
  isOpen?: boolean;
  disabled?: boolean;
  filtration?: boolean;
}>`
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 7px 0;
  width: 100%;
  border: transparent;
  background-color: transparent;
  font-size: 19px;
  line-height: normal;
  cursor: pointer;

  &:focus {
    outline: none;
  }

  ${({ theme, disabled, filtration, isOpen }) => css`
    ${disabled &&
    css`
      pointer-events: none;
      opacity: 0.5;
    `}

    ${!disabled &&
    css`
      pointer-events: visible;
      opacity: 1;
    `}
      
      ${filtration &&
    isOpen &&
    css`
      border-bottom: 2px solid ${theme.colors.categoryFilter.scrollBgGrey};
    `}
  `}
`;

export const Triangle = styled.span<{ isOpen?: boolean }>`
  width: 0;
  height: 0;
  border: 0 solid transparent;
  border-left-width: 8px;
  border-right-width: 8px;
  transition: all 0.4s ease;

  ${({ theme, isOpen }) => css`
    border-top: 10px solid ${theme.colors.categoryFilter.triangle};
    transform: ${isOpen && `rotate(180deg)`};
  `}
`;

export const ScrollWrapper = styled.div<{
  isOpen?: boolean;
  filtration?: boolean;
}>`
  max-height: 330px;
  overflow-y: auto;

  ${({ theme, filtration, isOpen }) => css`
    &::-webkit-scrollbar {
      width: 5px;
      border-radius: 10px;
      border: 1px solid white;
      background: ${theme.colors.categoryFilter.scrollBgGrey};
      opacity: 0;
    }

    &::-webkit-scrollbar-thumb {
      height: 100px;
      border-radius: 10px;
      background: ${theme.colors.categoryFilter.scrollBgBlue};
      opacity: 0;
    }

    ${filtration &&
    isOpen &&
    css`
      margin-top: 15px;
      max-height: 230px;
    `}
  `}
`;

export const SubCategories = styled.div<{
  isOpen: boolean;
  filtration?: boolean;
}>`
  display: grid;
  box-sizing: border-box;
  gap: 4px;
  overflow: hidden;
  transition: all 0.4s ease;

  ${({ isOpen, filtration }) => css`
    max-height: ${isOpen ? "2000px" : "0"};
    margin: ${isOpen ? "10px 0" : "0"};

    ${filtration &&
    isOpen &&
    css`
      gap: 15px;
      width: 100%;
      background-color: white;
    `}
  `}
`;

export const Cross = styled.div`
  ${({}) => css`
    &:before,
    &:after {
      content: "";
      position: absolute;
      top: 47%;
      right: 14px;
      width: 16px;
      height: 1.5px;
      background: transparent;
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

export const SubCategory = styled.div<{
  isCheck?: boolean;
  filtration?: boolean;
  notCheckbox?: boolean;
}>`
  position: relative;
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 7px 35px 7px 14px;
  margin: 0 16px 0 12px;
  border-radius: 5px;
  font-size: 14px;
  font-style: normal;
  font-weight: 400;
  line-height: normal;
  cursor: pointer;

  ${({ theme, isCheck, notCheckbox, filtration }) => css`
    color: ${theme.colors.colorGrey};

    ${theme.responsive.isDesktop &&
    css`
      font-size: 16px;
    `}

    ${isCheck &&
    notCheckbox &&
    !filtration &&
    css`
      background: ${theme.colors.categoryFilter.checkedCategory};
      color: ${theme.colors.white};

      &:hover {
        background: ${theme.colors.categoryFilter.checkedCategory};
      }

      ${Cross}::before, ${Cross}::after {
        background: ${theme.colors.white};
      }
    `} 

    ${!isCheck &&
    notCheckbox &&
    !filtration &&
    css`
      &:hover {
        background: ${theme.colors.categoryFilter.hoverCategory};
        color: ${theme.colors.blackColorText};
      }
    `}

    ${!notCheckbox &&
    !filtration &&
    css`
      padding: 7px 35px 7px 0;
    `}

    ${filtration &&
    css`
      padding: 0;
      margin: 0;
    `}
  `}
`;

export const OptionText = styled.div`
  width: 100%;
`;

export const LoaderContainer = styled.div`
  position: relative;
  display: inline-block;
  top: 0;
  right: 0;
  width: 25px;
  height: 25px;
`;

export const LoaderCircle = styled.div`
  position: absolute;
  box-sizing: border-box;
  display: block;
  width: 25px;
  height: 25px;
  border-radius: 50%;
  animation: ${rotate} 0.5s linear infinite;

  ${({ theme }) => css`
    border: 5px solid ${theme.colors.categoryFilter.locationBorder};
    border-color: ${theme.colors.categoryFilter.locationBorder} transparent
      transparent transparent;
  `}
`;
