import { Link } from 'react-router-dom';
import styled, { css } from 'styled-components';

import * as Icon from 'assets/icons';

export const DivWrap = styled.div`
  padding: 20px 0 22px;
  border-radius: 0 0 20px 20px;
  background-color: ${({ theme }) => theme.colors.white};
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.08);
`;

export const Wrapper = styled.div`
  padding: 0 15px;
  margin: 0 auto;
  max-width: 1250px;
  width: 100%;
  overflow: hidden;
`;

export const WrapMain = styled.div`
  display: flex;
  justify-content: space-between;
  align-items: center;
`;

export const LogoWrapper = styled(Link)`
  display: flex;
  align-items: center;
  margin-right: 40px;

  &:active {
    transform: scale(1.03);
  }
`;

export const Logo = styled(Icon.LogoSvg)`
  display: block;
  min-width: 58px;
  width: 58px;
  height: 58px;
  margin-right: 13px;
`;

export const LogoTitle = styled.h1`
  margin: 0;
  font-family: Expletus Sans, cursive;
  font-size: 26px;
  font-weight: 400;
  line-height: 35px;
  color: ${({ theme }) => theme.colors.btnBlue};
`;

export const WrapCategoriesTop = styled.div`
  transition: all 0.3s ease;
  opacity: 0;
  transform: translateY(-50%);
  position: relative;
  bottom: -13px;

  ${({ theme }) => css`
    color: ${theme.colors.btnBlue};

    &:after {
      position: absolute;
      right: -18px;
      bottom: 9px;
      content: '';
      border: 5px solid transparent;
      border-bottom: 5px solid ${theme.colors.btnBlue};
    }
  `}
`;

export const WrapCategoriesBottom = styled.div`
  transition: all 0.3s ease;
  position: relative;
  padding-right: 4px;

  &:after {
    position: absolute;
    right: -18px;
    bottom: 4px;
    content: '';
    border: 5px solid transparent;
    border-top: 5px solid ${({ theme }) => theme.colors.blackColorText};
  }
`;

export const WrapCategories = styled.div`
  position: absolute;
  height: 48px;
  font-size: 16px;
  font-weight: bold;
  line-height: 24px;
  margin: 25px 0 48px 179px;
  cursor: pointer;

  ${({ theme, open }) => css`
    color: ${theme.colors.blackColorText};

    ${open &&
    css`
      ${WrapCategoriesTop} {
        opacity: 1;
        transform: translateY(10px);
      }

      ${WrapCategoriesBottom} {
        opacity: 0;
        transform: translateY(50%);
      }
    `}

    ${!open &&
    css`
      ${WrapCategoriesBottom} {
        opacity: 1;
      }

      ${WrapCategoriesTop} {
        opacity: 0;
      }
    `}
  `}

  @media (min-width: 320px) {
    display: none;
  }
  @media (min-width: 540px) {
    display: block;
    margin-left: 265px;
  }
  @media (min-width: 1200px) {
    margin-left: 269px;
  }
`;

export const WrapSearch = styled.div`
  width: 100%;
  position: relative;

  @media (min-width: 540px) {
    max-width: 250px;
    margin-left: 120px;
    margin-right: 22px;
  }
  @media (min-width: 950px) {
    max-width: 300px;
  }
  @media (min-width: 1024px) {
    max-width: 400px;
  }
  @media (min-width: 1200px) {
    max-width: 523px;
  }
`;

export const InputSearch = styled.input`
  padding: 10px 38px 10px 60px;
  width: 100%;
  max-width: 523px;
  height: 50px;
  font-style: italic;
  font-size: 16px;
  line-height: 24px;
  border: 1px solid ${({ theme }) => theme.colors.activeColor};
  border-radius: 25px;
  box-sizing: border-box;
  outline: none;
`;

export const IconBlock = styled.div`
  position: absolute;
  display: flex;
  flex-shrink: 0;
  top: 50%;
  right: 15px;
  cursor: pointer;
  transform: translateY(-50%);
`;

export const LabelLink = styled.div`
  position: absolute;
  display: flex;
  justify-content: center;
  align-items: center;
  top: 5px;
  left: 5px;
  box-sizing: border-box;
  border-radius: 20px;
  width: 40px;
  height: 40px;
  background-color: ${({ theme }) => theme.colors.btnBlue};
  cursor: pointer;
`;
