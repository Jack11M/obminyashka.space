import styled, { css } from "styled-components";

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
      max-height: 160px;
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
    `}
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

export const styleSet = css`
  padding: 38px 22px 38px 25px;
  width: 290px;
  border: 2px dashed ${({ theme }) => theme.colors.btnBlue};
  border-radius: 20px;
`;

export const BlockStyleSet = css`
  display: flex;
  flex-direction: column;
  gap: 12px;
  margin-top: 20px;
`;

export const CategoryFilter = styled.div`
  ${styleSet};
`;

export const Filter = styled.div`
  ${styleSet};
  margin-top: 20px;
`;

export const SelectBlock = styled.div`
  ${BlockStyleSet};
`;

export const CheckBoxBlock = styled.div`
  ${BlockStyleSet};
`;

// export const Title = styled.div`
//   font-weight: 700;
//   font-size: 19px;
//   line-height: 24px;
//   text-transform: uppercase;
//   color: ${({ theme }) => theme.colors.btnBlue};
// `;

export const TitleOfEachCategory = styled.div`
  font-size: 18px;
  line-height: 24px;
`;

export const Close = styled.div<{ isSelected: boolean }>`
  display: flex;
  flex-shrink: 0;

  ${({ theme, isSelected }) => css`
    opacity: ${isSelected ? 1 : 0};

    svg {
      path {
        fill: ${theme.colors.white};
      }
    }
  `}
`;

export const ScrollBar = styled.div`
  display: flex;
  flex-direction: column;
  gap: 15px;
  height: 425px;
  overflow: auto;

  ::-webkit-scrollbar {
    width: 8px;
    height: 10px;
  }
`;
