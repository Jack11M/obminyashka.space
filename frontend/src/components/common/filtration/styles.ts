import styled, { css } from "styled-components";

export const FiltrationWrapper = styled.div`
  width: 300px;

  ${({ theme }) => css`
    ${theme.responsive.isDesktopMD &&
    css`
      width: 350px;
    `}

    ${theme.responsive.isDesktopLG &&
    css`
      width: 370px;
    `}
  `}
`;

export const CategoryWrapper = styled.div`
  padding: 25px;
  margin-bottom: 50px;
  border-radius: 20px;
  border: 2px dashed ${({ theme }) => theme.colors.categoryFilter.border};
`;

export const CategoryTitle = styled.h1`
  margin: 0 0 10px;
  font-size: 22px;
  text-transform: uppercase;
  color: ${({ theme }) => theme.colors.categoryFilter.title};
`;

export const CategoryUnderline = styled.div`
  margin-bottom: 20px;
  height: 1px;
  width: 100%;
  background-color: #d1d1d1;
`;

export const LocationTitleContainer = styled.div`
  display: flex;
  justify-content: space-between;
  align-items: center;
`;

export const LocationTitle = styled.input<{
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
      border-bottom: 2px solid ${theme.colors.categoryFilter.inputBorderBottom};
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

export const SubCategories = styled.div<{
  isOpen: boolean;
  filtration?: boolean;
}>`
  box-sizing: border-box;
  display: grid;
  position: relative;
  height: 97px;
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
      overflow: visible;
    `}
  `}
`;

export const SubCategory = styled.div<{
  isCheck?: boolean;
  filtration?: boolean;
  notCheckbox?: boolean;
}>`
  display: flex;
  position: absolute;
  top: 0;
  width: 100%;
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
  z-index: 999;

  ${({ theme, filtration }) => css`
    color: ${theme.colors.colorGrey};

    ${theme.responsive.isDesktop &&
    css`
      font-size: 16px;
    `}

    ${filtration &&
    css`
      padding: 0;
      margin: 0;
    `}
  `}
`;
