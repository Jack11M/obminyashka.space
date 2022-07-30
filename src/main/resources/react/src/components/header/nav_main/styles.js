import { Link } from 'react-router-dom';
import styled, { css } from 'styled-components';

import logo from 'assets/img/logo.png';

export const DivWrap = styled.div`
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
  font-size: 16px;
  font-weight: bold;
  line-height: 24px;
  color: ${({ theme }) => theme.colors.btnBlueActive};
  margin: 25px 0 0 179px;
  padding-bottom: 95px;
  cursor: pointer;

  &:hover {
    :before {
      opacity: 1;
      transform: translateY(0);
    }
    ::after {
      opacity: 0;
      transform: translateY(50%);
    }
    &:last-child {
      left: 0;
      transform: translateY(0);
    }
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
  transition: all 0.3s ease;

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
`;

export const WrapCategoriesBottom = styled.div`
  transition: all 0.3s ease;

  position: relative;

  &:after {
    position: absolute;
    right: -18px;
    bottom: 4px;
    content: '';
    border: 5px solid transparent;
    border-top: 5px solid ${({ theme }) => theme.colors.btnBlueActive};
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
  padding: 10px 10px 10px 60px;
  width: 100%;
  max-width: 541px;
  height: 50px;

  font-style: italic;
  font-size: 16px;
  line-height: 24px;
  border: 1px solid ${({ theme }) => theme.colors.btnBlueActive};
  border-radius: 25px;
  box-sizing: border-box;
  outline: none;
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
