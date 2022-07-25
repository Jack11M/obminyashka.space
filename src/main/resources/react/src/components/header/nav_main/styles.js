import { Link } from 'react-router-dom';
import styled, { css } from 'styled-components';

import logo from 'assets/img/logo.png';

export const DivWrap = styled.div`
  font-family: Roboto, sans-serif;
  padding: 20px 0 22px;
  border-radius: 0 0 20px 20px;
  background-color: #fff;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.08);
`;

export const Wrapper = styled.div`
  margin: 0 auto;
  max-width: 1251px;
  overflow: hidden;
  padding: 0 15px;
  width: 100%;
`;

export const WrapMain = styled.div`
  display: flex;
  justify-content: space-between;
  align-items: center;
`;

export const LogoLink = styled(Link)`
  display: block;
  min-width: 148px;
  width: 148px;
  height: 56px;
  background: url(${logo});
  margin-right: 40px;

  &:active {
    transform: scale(1.03);
  }
  @media (min-width: 320px) {
    display: none;
  }
  @media (min-width: 540px) {
    display: block;
  }
`;

export const WrapCategories = styled.div`
  position: absolute;
  height: 56px;
  font-family: Roboto, sans-serif;
  font-size: 16px;
  font-weight: bold;
  line-height: 24px;
  color: ${({ theme }) => theme.colors.btnBlueActive};
  margin: 25px 0 0 179px;
  padding-bottom: 95px;
  cursor: pointer;

  &:hover {
    left: 0;
    transform: translateY(0);
  }
  @media (min-width: 320px) {
    display: none;
  }
  @media (min-width: 540px) {
    display: block;
    margin-left: 149px;
  }
  @media (min-width: 840px) {
    margin-left: 164px;
  }
  @media (min-width: 1200px) {
    margin-left: 179px;
  }
`;

export const WrapCategoriesTop = styled.div`
  opacity: 0;
  transform: translateY(-50%);
  position: relative;
  bottom: -13px;
  ${({ theme }) => css`
    color: ${theme.colors.blackColorText};

    &:after {
      position: absolute;
      right: -18px;
      bottom: 9px;
      content: '';
      border: 5px solid transparent;
      border-bottom: 5px solid ${theme.colors.btnBlueActive};
    }
  `}
  &:hover {
    opacity: 1;
    transform: translateY(0);
  }
`;

export const WrapCategoriesBottom = styled.div`
  position: relative;

  &:after {
    position: absolute;
    right: -18px;
    bottom: 4px;
    content: '';
    border: 5px solid transparent;
    border-top: 5px solid ${({ theme }) => theme.colors.btnBlueActive};
  }
  &:hover {
    opacity: 0;
    transform: translateY(50%);
  }
`;

export const WrapSearch = styled.div`
  max-width: 541px;
  width: 100%;
  position: relative;
  margin-left: 60px;
  margin-right: 30px;
  @media (min-width: 540px) {
    margin-right: 8px;
  }
  @media (min-width: 750px) {
    margin-right: 30px;
    margin-left: 80px;
  }
  @media (min-width: 950px) {
    width: 30%;
  }
  @media (min-width: 1200px) {
    width: 100%;
  }
  @media (min-width: 1300px) {
    margin-left: 60px;
  }
`;

export const InputSearch = styled.input`
  max-width: 541px;
  width: 100%;
  height: 50px;
  outline: none;

  border: 1px solid ${theme.colors.btnBlueActive};
  border-radius: 25px;
  font-family: Roboto, sans-serif;
  font-style: italic;
  font-size: 16px;
  line-height: 24px;

  box-sizing: border-box;
  padding: 10px 10px 10px 60px;
`;

export const Label = styled.div`
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
  background-color: ${({ theme }) => theme.colors.btnBlueActive};
  cursor: pointer;
`;
