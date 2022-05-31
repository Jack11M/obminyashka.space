import styled, { css } from 'styled-components';

export const InputDiv = styled.div`
  position: relative;
  margin-bottom: ${({ path }) => (path ? '42px' : '22px')};
  &:last-child {
    margin-bottom: ${({ path }) => (path ? '26px' : '32px')};
  }
`;

export const Label = styled.label`
  font-style: normal;
  font-weight: normal;
  font-size: 14px;
  line-height: 22px;
  color: ${({ theme: { colors } }) => colors.colorGrey};
  cursor: pointer;
`;

export const InputAuth = styled.input`
  padding: 12px 16px;
  width: 100%;
  border-radius: 2px;
  box-sizing: border-box;
  font-family: Roboto, sans-serif;
  font-style: normal;
  font-weight: normal;
  font-size: 16px;
  line-height: 24px;
  outline: none;
  ${({ theme, error }) => css`
    border: 1px solid ${error ? theme.colors.colorError : 'hsl(0, 0%, 74%)'};
    color: ${theme.colors['right-color-text']};

    &:focus {
      border-color: ${error ? theme.colors.colorError : 'hsl(0, 0%, 44%)'};
    }
  `}
`;

export const SpanError = styled.span`
  position: absolute;
  top: 67px;
  left: 0;
  font-size: 12px;
  font-style: normal;
  font-weight: 400;
  line-height: 20px;

  ${({ theme, error }) => css`
    ${InputAuth} {
      border-color: ${error && theme.colors.colorError};
    }
    color: ${theme.colors.colorError};
  `}
`;
