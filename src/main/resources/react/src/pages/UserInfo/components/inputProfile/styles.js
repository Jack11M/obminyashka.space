/* eslint-disable indent */
import styled, { css } from 'styled-components';

export const ProfileInput = styled.div`
  position: relative;
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 21px;
`;

export const Label = styled.label`
  font-size: 14px;
  line-height: 16px;
  display: inline-flex;
  cursor: pointer;
`;

export const Input = styled.input`
  display: inline-flex;
  box-sizing: border-box;
  width: 415px;
  padding: 9px 16px 9px 16px;
  border: 1px solid #bcbcbc;
  border-radius: 2px;
  outline: none;
  font-size: 16px;
  line-height: 16px;
  font-family: inherit;
  ${({ theme, error }) => css`
    border: 1px solid ${error ? theme.colors.colorError : 'hsl(0, 0%, 74%)'};
    color: ${theme.colors['right-color-text']};

    &:focus,
    &:hover {
      border-color: ${error ? theme.colors.colorError : 'hsl(0, 0%, 44%)'};
    }
  `}

  &::placeholder {
    color: #a3a3a3;
  }
`;

export const SpanError = styled.span`
  position: absolute;
  bottom: -17px;
  left: 135px;
  font-size: 11px;
  font-style: normal;
  font-weight: 400;
  line-height: 20px;
  color: ${({ theme: { colors } }) => colors.colorError};
`;
