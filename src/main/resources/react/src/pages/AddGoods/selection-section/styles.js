import styled, { css } from 'styled-components';

export const AddChoose = styled.div`
  margin-bottom: 50px;
`;

export const Sections = styled.div`
  display: flex;
  justify-content: space-between;
  flex-wrap: wrap;
`;

export const SectionsItem = styled.div`
  max-width: 350px;
  width: 100%;
`;

export const ItemDescription = styled.h5`
  margin-bottom: 10px;
  white-space: nowrap;
  color: ${({ theme }) => theme.colors.colorGrey};
`;

export const InputText = styled.input`
  box-sizing: border-box;
  padding: 10px 10px 10px 16px;
  max-width: 350px;
  width: 100%;
  height: 48px;
  border-radius: 2px;
  outline: none;
  line-height: 26px;
  font-size: 16px;
  font-family: 'Roboto', sans-serif;

  ${({ theme, p }) => css`
    border: 1px solid
      ${p.error ? p.theme.colors.colorError : theme.colors.borderColor};
    color: ${theme.colors.colorInput};
    caret-color: ${theme.colors.activeColor};
  `};

  &:focus {
    border-color: hsl(0, 0%, 44%);
  }
`;
