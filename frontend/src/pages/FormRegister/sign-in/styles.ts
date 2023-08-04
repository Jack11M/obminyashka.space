import styled, { css } from "styled-components";

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

export const ExtraButton = styled.button`
  font-family: Roboto;
  font-size: 14px;
  font-style: normal;
  font-weight: 400;
  line-height: normal;
  color: ${({ theme }) => theme.colors.auth.titleBottomLine};
`;
