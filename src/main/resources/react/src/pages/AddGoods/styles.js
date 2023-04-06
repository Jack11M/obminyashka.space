import styled from 'styled-components';

export const MainContainer = styled.main`
  padding-top: 200px;
  padding-bottom: 50px;
`;

export const Container = styled.div`
  padding: 0 15px;
  margin: auto;
  max-width: 1251px;
`;

export const AddContainer = styled.div`
  padding: 40px;
  min-height: 600px;
  width: 100%;
  background-color: ${({ theme }) => theme.colors.white};
`;

export const Wrap = styled.div`
  margin-bottom: 50px;
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
  margin-bottom: 13px;
  font-style: normal;
  font-weight: 400;
  font-size: 22px;
  line-height: 27px;
  color: ${(p) => (p.error ? p.theme.colors.colorError : '#383838')};
`;

export const Star = styled.span`
  color: ${({ theme }) => theme.colors.colorRed};
`;

export const WrapFiles = styled.div`
  margin-bottom: 100px;
`;

export const FileDescription = styled.p`
  color: ${({ theme }) => theme.colors.borderColor};
  font-size: 16px;
  line-height: 26px;
  font-weight: 400;
`;

export const WrapperFile = styled.div`
  display: grid;
  grid-template-columns: repeat(5, 1fr);
  margin-top: 15px;
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

export const TextAreaBlock = styled.div`
  margin-bottom: 20px;
`;
