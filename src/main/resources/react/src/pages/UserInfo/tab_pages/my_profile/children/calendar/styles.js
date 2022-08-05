import styled, { css } from 'styled-components';

export const Container = styled.div`
  position: relative;
  display: flex;
  justify-content: space-between;
  align-items: center;
  gap: 10px;
  margin-bottom: 20px;

  ${({ theme }) => css`
    .react-datepicker {
      padding: 8px;
      border-radius: 16px;

      &__header {
        background-color: transparent;
        border-radius: 16px 16px 0 0;
        border: 2px solid ${theme.colors.btnBlue};
      }

      &__day-name {
        color: ${theme.colors.btnBlue};
        font-size: 16px;
        font-weight: 700;
        text-transform: capitalize;
        user-select: none;
      }

      &__day {
        font-size: 14px;
        font-weight: 600;
        user-select: none;

        :hover:not(.react-datepicker__day--disabled):not(.react-datepicker__day--selected):not(.react-datepicker__day--keyboard-selected) {
          background-color: #12b6ed1a;
        }
      }

      &__day--today {
        color: ${theme.colors.btnBlue};
        border: 1px solid ${theme.colors.btnBlue};
        border-radius: 10px;
      }

      &__day--selected,
      &__day--keyboard-selected {
        background-color: ${theme.colors.btnBlue};
        color: ${theme.colors.white};
        border-radius: 10px;
      }

      &__day--outside-month {
        opacity: 0.8;
        font-weight: 400;
      }

      &__day--disabled {
        font-size: 14px;
        opacity: 0.5;
        font-weight: 400;
      }
    }
  `}
`;

export const Label = styled.label`
  font-size: 14px;
  line-height: 16px;
  display: inline-flex;
  cursor: pointer;
  align-self: flex-start;
  margin-top: 13px;
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
    color: ${({ theme }) => theme.colors.colorTextDisabled};
  }
`;

export const ErrorSpan = styled.span`
  font-weight: 400;
  font-size: 12px;
  line-height: 20px;
  margin-top: 8px;
  color: ${({ theme }) => theme.colors.colorError};
`;
