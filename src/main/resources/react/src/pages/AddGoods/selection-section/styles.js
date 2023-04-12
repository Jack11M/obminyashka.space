import styled from 'styled-components';

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
