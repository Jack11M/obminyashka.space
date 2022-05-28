import styled from 'styled-components';

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
  border: 1px solid
    ${({ theme: { colors }, error }) =>
      error ? colors.colorError : 'hsl(0, 0%, 74%)'};
  color: ${({ theme: { colors } }) => colors['right-color-text']};

  &:focus {
    border-color: ${({ theme: { colors }, error }) =>
      error ? colors.colorError : 'hsl(0, 0%, 44%)'};
  }
`;

export const SpanError = styled.span`
  position: absolute;
  top: 67px;
  left: 0;
  font-size: 12px;
  font-style: normal;
  font-weight: 400;
  line-height: 20px;

  ${InputAuth} {
    border-color: ${({ theme: { colors }, error }) =>
      error && colors.colorError};
  }
  color: ${({ theme: { colors } }) => colors.colorError};
`;
