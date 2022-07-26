import styled, { css } from 'styled-components';
import { Link } from 'react-router-dom';

const styleSet = css`
  position: relative;
  display: block;
  min-width: 295px;
  border-radius: 25px;
  padding: 13px 35px 13px 61px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  border: 0;
  cursor: pointer;

  ${({ theme }) => css`
    color: ${theme.colors.white};
    background-color: ${theme.colors.btnGreen};

    &:before {
      position: absolute;
      content: '';
      top: 23px;
      left: 33px;
      width: 14px;
      height: 2px;
      background-color: ${theme.colors.white};
    }

    &:after {
      position: absolute;
      content: '';
      top: 17px;
      left: 39px;
      width: 2px;
      height: 14px;
      background-color: ${theme.colors.white};
    }

    &:hover,
    &:active {
      background-color: ${theme.colors.btnGreenActive};
    }
  `}
`;

export const StylizedLink = styled(Link)`
  ${styleSet};
`;

export const SubmitButton = styled.button`
  ${styleSet};
`;

export const Span = styled.span`
  font-size: 16px;
  line-height: 24px;
  text-transform: uppercase;
`;
