import styled, { css } from "styled-components";
import { Link } from "react-router-dom";

export const Wrapper = styled.div`
  padding: 0 15px;

  ${({ theme }) => css`
    ${theme.responsive.isTablet &&
    css`
      padding: 0 45px;
    `}
  `}
`;

export const WrapperInputSingIn = styled.div`
  display: flex;
  flex-direction: column;
  gap: 26px;
  padding-top: 3px;
  margin-bottom: 25px;

  ${({ theme }) => css`
    ${theme.responsive.isTablet &&
    css`
      gap: 42px;
    `}
  `}
`;

export const Extra = styled.div`
  display: -webkit-box;
  display: -ms-flexbox;
  display: flex;
  -webkit-box-pack: justify;
  -ms-flex-pack: justify;
  justify-content: space-between;
  margin-bottom: 44px;

  ${({ theme }) => css`
    ${theme.responsive.isTablet &&
    css`
      margin-bottom: 30px;
    `}
  `}
`;

export const ExtraLink = styled(Link)`
  font-family: Roboto;
  font-size: 14px;
  font-style: normal;
  font-weight: 400;
  line-height: normal;
  color: #29a5d4;
`;

export const WrapperButtons = styled.div`
  margin: 0 auto;
  width: 263px;
  max-width: 290px;

  ${({ theme }) => css`
    ${theme.responsive.isTablet &&
    css`
      width: 100%;
    `}
  `}
`;

export const FirstText = styled.div`
  margin: 20px 0 16px;
  font-size: 16px;
  font-style: normal;
  font-weight: 700;
  line-height: 24px;
  text-align: center;
  text-transform: uppercase;
  color: #12b6ed;
`;

export const SecondText = styled.div`
  margin-bottom: 24px;
  text-align: center;
  font-size: 14px;
  font-style: normal;
  font-weight: 400;
  line-height: 22px;
  color: #8e8e8e;
`;

export const ButtonsRegistration = styled.div`
  display: flex;
  justify-content: space-between;
  margin: 0 auto 45px;
  width: 245px;

  ${({ theme }) => css`
    ${theme.responsive.isTablet &&
    css`
      width: 100%;
    `}

    ${theme.responsive.isDesktop &&
    css`
      margin: 0 auto 60px;
    `}
  `}
`;
