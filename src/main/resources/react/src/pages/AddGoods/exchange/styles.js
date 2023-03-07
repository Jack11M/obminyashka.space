/* eslint-disable indent */
import styled, { css } from 'styled-components';

export const Wrap = styled.div`
  margin-bottom: 50px;
`;

export const TitleH3 = styled.h3`
  font-family: 'Open Sans', sans-serif;
  font-size: 26px;
  font-weight: 600;
  margin-bottom: 10px;
  line-height: 40px;
`;

export const Description = styled.p`
  color: ${({ theme }) => theme.colors.colorGrey};
  margin-bottom: 0;
`;

export const Explanation = styled.p`
  color: ${({ theme }) => theme.colors.colorGrey};
  font-size: 14px;
  margin-bottom: 10px;
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
  margin: 6px 7px;
  padding: 6px 34px 6px 14px;
  background-color: ${({ theme }) => theme.colors.btnBlue};
  border-radius: 5px;
  color: ${({ theme }) => theme.colors.white};
  text-transform: uppercase;
  font-size: 12px;
  line-height: 24px;
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

  &:after {
    position: absolute;
    content: '';
    width: 1px;
    height: 11px;
    background: #fff;
    transform: rotate(45deg);
  }

  &:before {
    position: absolute;
    content: '';
    width: 1px;
    height: 11px;
    background: #fff;
    transform: rotate(-45deg);
  }

  &:hover {
    transform: rotate(90deg);
  }
`;

export const ChangeInput = styled.input`
  position: relative;
  min-width: 262px;
  box-sizing: border-box;
  height: 32px;
  margin: 8px 7px;
  font-family: 'Roboto', sans-serif;
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
