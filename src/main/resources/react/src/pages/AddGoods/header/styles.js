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
  width: 100%;`

const ItemDescription = styled.h5`
  color: #8e8e8e;
  margin-bottom: 10px;
  white-space: nowrap;
`;

const Select = styled.div`
  display: flex;
  align-items: center;
  border: 1px solid #bcbcbc;
  border-radius: 2px;
  max-width: 350px;
  height: 50px;
`;

const Image = styled.img`
  width: 18px;
  height: 18px;
  margin-left: 15px;
`;

const SelectTitle = styled.p`
  padding: 10px;
  line-height: 26px;
  color: #11171f;
`;

const InputText = styled.input`
  box-sizing: border-box;
  max-width: 350px;
  width: 100%;
  height: 50px;
  border: 1px solid #bcbcbc;
  border-radius: 2px;
  outline: none;
  padding: 10px;
  line-height: 26px;
  color: #11171f;
  font-size: 16px;
`;

export {
  Image,
  Select,
  TitleH3,
  Sections,
  AddChoose,
  InputText,
  SelectTitle,
  SectionsItem,
  ItemDescription,
};
