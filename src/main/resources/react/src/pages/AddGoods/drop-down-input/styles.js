import styled, { css } from 'styled-components';

const Wrap = styled.div`
  position: relative;
  width: 350px;
  z-index: 1;
`;

const Label = styled.label`
  display: block;
  margin-bottom: 10px;
  font-family: Roboto, sans-serif;
  font-size: 16px;
  line-height: 24px;
  color: ${({ theme }) => theme.colors.colorGrey};
`;

const Input = styled.input`
  width: 100%;
  height: 48px;
  padding: 0 30px 0 14px;
  line-height: 24px;
  font-size: 16px;
  border: 1px solid ${(p) => (p.error ? p.theme.colors.colorError : '#bcbcbc')};
  border-radius: 2px;
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

const WrapSvg = styled.div`
  position: absolute;
  right: 5px;
  bottom: 10px;
  cursor: pointer;
`;

const WrapDropItems = styled.div`
  position: absolute;
  width: 100%;
  max-height: 470px;
  background-color: ${({ theme }) => theme.colors.white};
  border: 1px solid hsl(0, 0%, 44%);
  border-top: none;
  overflow-y: auto;
  z-index: 2;
`;

const SelectedItem = styled.div`
  display: flex;
  align-items: center;
  border-bottom: 1px solid hsl(0, 0%, 44%);
  cursor: pointer;
  transition: ease-in-out 0.3s;

  &:last-child {
    border-bottom: none;
  }

  & > p {
    padding: 10px;
    line-height: 26px;
    color: #11171f;
  }

  &:hover {
    background-color: hsl(195, 100%, 90%);
  }
`;

export { Label, Input, WrapSvg, SelectedItem, Wrap, WrapDropItems };
