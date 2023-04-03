import styled from 'styled-components';

export const MainContainer = styled.main`
  padding-top: 200px;
  padding-bottom: 50px;
`;

export const Container = styled.div`
  max-width: 1251px;
  padding: 0 15px;
  margin: auto;
`;

export const AddContainer = styled.div`
  min-height: 600px;
  width: 100%;
  background-color: ${({ theme }) => theme.colors.white};
  padding: 40px;
`;

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

export const WrapItems = styled.div`
  display: flex;
  flex-wrap: wrap;
  justify-content: space-between;
  margin-bottom: 50px;
`;

export const SectionsItem = styled.div`
  font-size: inherit;
`;

export const ItemTittle = styled.h4`
  font-style: normal;
  font-weight: 400;
  font-size: 22px;
  margin-bottom: 13px;
  line-height: 27px;
  color: ${(p) => (p.error ? p.theme.colors.colorError : '#383838')};
`;

export const WrapDescription = styled.div`
  padding-bottom: 40px;
`;

export const DescriptionText = styled.p`
  color: ${({ theme }) => theme.colors.colorGrey};
  margin-bottom: 10px;
`;

export const Star = styled.span`
  color: ${({ theme }) => theme.colors.colorRed};
`;

export const TextArea = styled.textarea`
  margin-top: 16px;
  padding: 10px;
  width: 100%;
  min-height: 150px;
  font-size: 16px;
  line-height: 24px;
  outline: none;
  resize: none;
  border: 1px solid #bcbcbc;
  border-radius: 2px;
  caret-color: ${({ theme }) => theme.colors.activeColor};

  &:focus {
    border-color: hsl(0, 0%, 44%);
  }
`;

export const WrapFiles = styled.div`
  margin-bottom: 100px;
`;

export const FileTittle = styled.h3`
  font-weight: 600;
  color: #11171f;
  font-size: 26px;
  line-height: 40px;
`;

export const FileDescription = styled.p`
  font-size: 16px;
  line-height: 26px;
  color: #8f8f8f;
  font-weight: 400;
`;

export const WrapperFile = styled.div`
  margin-top: 15px;
  display: grid;
  grid-template-columns: repeat(5, 1fr);
`;

export const WrapButtons = styled.div`
  display: flex;
  justify-content: space-between;
  align-items: center;
`;

export const BlockButtons = styled.div`
  display: flex;
  gap: 30px;
`;

export const StyledInputField = styled.div`
  margin-bottom: 20px;
`;
