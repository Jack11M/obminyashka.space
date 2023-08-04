import styled, { css } from "styled-components";

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
  color: ${({ theme }) => theme.colors.auth.firstText};
`;

export const SecondText = styled.div`
  margin-bottom: 24px;
  text-align: center;
  font-size: 14px;
  font-style: normal;
  font-weight: 400;
  line-height: 22px;
  color: ${({ theme }) => theme.colors.auth.secondText};
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
