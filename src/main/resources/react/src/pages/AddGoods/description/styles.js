import styled, { css } from 'styled-components';

export const WrapDescription = styled.div`
  position: relative;
  padding-bottom: 40px;
`;

export const DescriptionText = styled.p`
  margin-bottom: 10px;
  color: ${({ theme }) => theme.colors.colorGrey};
`;

export const TextArea = styled.textarea`
  padding: 10px;
  margin-top: 16px;
  width: 100%;
  min-height: 150px;
  font-size: 16px;
  line-height: 24px;
  outline: none;
  resize: none;
  border-radius: 2px;

  ${({ theme, error }) => css`
    caret-color: ${theme.colors.activeColor};
    border: 1px solid ${error ? theme.colors.colorError : '#bcbcbc'};

    &:focus {
      border-color: ${error ? theme.colors.colorError : 'hsl(0, 0%, 44%)'};
    }
  `}
`;

export const TitleH3 = styled.h3`
  font-family: 'Open Sans', sans-serif;
  font-size: 26px;
  font-weight: 600;
  margin-bottom: 10px;
  line-height: 40px;
`;

export const ErrorCount = styled.span`
  position: absolute;
  right: 4px;
  bottom: 47px;
  ${({ theme, error }) => css`
    color: ${error ? theme.colors.colorError : theme.colors.colorTextDisabled};
  `}
`;
