import styled from 'styled-components';

const AddChoose = styled.div`
  margin-bottom: 50px;
`;

const TitleH3 = styled.h3`
  font-family: 'Open Sans', sans-serif;
  font-size: 26px;
  font-weight: 600;
  margin-bottom: 10px;
  line-height: 40px;
`;

const Sections = styled.div`
  display: flex;
  justify-content: space-between;
  flex-wrap: wrap;
`;

const SectionsItem = styled.div`
  max-width: 350px;
  width: 100%;
`;

const ItemDescription = styled.h5`
  color: #8e8e8e;
  margin-bottom: 10px;
  white-space: nowrap;
`;

const InputText = styled.input`
  box-sizing: border-box;
  max-width: 350px;
  width: 100%;
  height: 48px;
  border: 1px solid ${(p) => (p.error ? p.theme.colors.colorError : '#bcbcbc')};
  border-radius: 2px;
  outline: none;
  padding: 10px 10px 10px 16px;
  color: #11171f;
  line-height: 26px;
  font-size: 16px;
  font-family: 'Roboto', sans-serif;
  caret-color: ${({ theme }) => theme.colors.activeColor};

  &:focus {
    border-color: hsl(0, 0%, 44%);
  }
`;

const Star = styled.span`
  color: ${({ theme }) => theme.colors.colorRed};
`;

export {
  Star,
  TitleH3,
  Sections,
  AddChoose,
  InputText,
  SectionsItem,
  ItemDescription,
};
