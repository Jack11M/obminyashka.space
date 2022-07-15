import styled, { css } from 'styled-components';

export const Container = styled.div`
  position: relative;
  display: flex;
  justify-content: space-between;
  align-items: center;
  gap: 10px;
  margin-bottom: 20px;

  .react-datepicker {
    padding: 8px;
    border-radius: 16px;

    &__header {
      background-color: transparent;
      border-radius: 16px 16px 0 0;
      border: 2px solid #12b6ed;
    }

    &__day-name {
      color: #12b6ed;
      font-size: 16px;
      font-weight: 700;
      text-transform: capitalize;
      user-select: none;
    }

    &__day {
      font-size: 14px;
      font-weight: 600;
      user-select: none;

      :hover:not(.react-datepicker__day--disabled) {
        background-color: #12b6ed1a;
      }
    }

    &__day--today {
      font-size: 18px;
      font-weight: 700;
    }

    &__day--selected,
    &__day--keyboard-selected {
      background-color: #12b6ed;
      color: #ffffff;
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
