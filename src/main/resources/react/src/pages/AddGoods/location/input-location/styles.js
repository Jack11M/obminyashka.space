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
  color: #8e8e8e;
`;

const Input = styled.input`
  width: 100%;
  height: 48px;
  padding: 0 14px;
  line-height: 24px;
  font-size: 16px;
  border: 1px solid #bcbcbc;
  border-radius: 2px;
  caret-color: #00c0ff;

  &:focus {
    border-color: hsl(0, 0%, 44%);
  }
  ${(p) =>
    p.focus &&
    css`
      border-color: hsl(0, 0%, 44%);
    `};
`;
const WrapDropItems = styled.div`
  position: absolute;
  width: 100%;
  max-height: 470px;
  background-color: #fff;
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

export { Label, Input, SelectedItem, Wrap, WrapDropItems };
