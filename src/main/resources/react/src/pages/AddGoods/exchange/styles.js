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
  color: #8e8e8e;
  margin-bottom: 0;
`;

export const Explanation = styled.p`
  color: #8e8e8e;
  font-size: 14px;
  margin-bottom: 10px;
`;

export const ChangeWrapp = styled.div`
  align-items: center;
  border: 1px solid #bcbcbc;
  border-radius: 2px;
  display: inline-flex;
  flex-wrap: wrap;
  min-width: 350px;
`;

export const ChangeItem = styled.div`
  position: relative;
  margin: 6px 7px;
  padding: 6px 34px 6px 14px;
  background: #12b6ed;
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
export const WrapInput = styled.div``;

export const ChangeInput = styled.input`
  position: relative;
  min-width: 262px;
  box-sizing: border-box;
  height: 32px;
  margin: 7px;
  border: none;
  font-family: 'Roboto', sans-serif;
  line-height: 24px;
  outline: none;
  caret-color: #00c0ff;
  ${({ theme, error }) => css`
    border-color: ${error ? theme.colors.colorError : 'hsl(0, 0%, 74%)'};

    &:focus {
      border-color: ${error ? theme.colors.colorError : 'hsl(0, 0%, 44%)'};
    }
  `}
`;

export const Star = styled.span`
  color: red;
`;
