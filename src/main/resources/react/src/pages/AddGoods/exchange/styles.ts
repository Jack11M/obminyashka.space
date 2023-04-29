/* eslint-disable indent */
import styled, { css } from 'styled-components';

export const Wrap = styled.div`
  margin-bottom: 50px;
`;

export const Description = styled.p`
  margin-bottom: 0;
  color: ${({ theme }) => theme.colors.colorGrey};
`;

export const Explanation = styled.p`
  margin-bottom: 10px;
  color: ${({ theme }) => theme.colors.colorGrey};
  font-size: 14px;
`;

export const ChangeWrap = styled.div`
  display: inline-flex;
  align-items: center;
  flex-wrap: wrap;
  min-width: 350px;
  border-radius: 2px;

  border: 1px solid
    ${({ theme, borderValue, error }) => {
      if (borderValue && !error) return 'hsl(0,0%,44%)';
      if (error) return theme.colors.colorError;
      return '#bcbcbc';
    }};
`;

export const ChangeItem = styled.div`
  position: relative;
  padding: 6px 34px 6px 14px;
  margin: 6px 7px;
  border-radius: 5px;
  text-transform: uppercase;
  font-size: 12px;
  line-height: 24px;

  ${({ theme }) => css`
    background-color: ${theme.colors.btnBlue};
    color: ${theme.colors.white};
  `};
`;

export const Span = styled.span`
  position: absolute;
  display: inline-flex;
  justify-content: center;
  align-items: center;
  top: calc(50% - 8px);
  width: 16px;
  height: 16px;
  right: 8px;
  cursor: pointer;
  transition: 0.3s ease-in-out;

  &:before,
  &:after {
    position: absolute;
    content: '';
    width: 1px;
    height: 11px;
    background: ${({ theme }) => theme.colors.white};
  }

  &:after {
    transform: rotate(45deg);
  }

  &:before {
    transform: rotate(-45deg);
  }

  &:hover {
    transform: rotate(90deg);
  }
`;

export const ChangeInput = styled.input`
  position: relative;
  margin: 8px 7px;
  box-sizing: border-box;
  min-width: 262px;
  height: 32px;
  line-height: 24px;
  border: none;
  outline: none;
  caret-color: ${({ theme }) => theme.colors.activeColor};

  &:focus {
    border-color: hsl(0, 0%, 44%);
  }
  ${(p) =>
    p.focus &&
    css`
      border-color: hsl(0, 0%, 44%);
    `};
`;
