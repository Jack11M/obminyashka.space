import styled from 'styled-components';

export const Container = styled.div`
  display: flex;
  margin-top: 156px;
  width: 100%;
  min-height: calc(100vh - 376px - 157px);
`;

export const Aside = styled.aside`
  display: flex;
  flex-direction: column;
  align-items: flex-end;
  margin-top: -20px;
  width: 25%;
  min-height: 100%;
  background-color: ${({ theme }) => theme.colors.white};
  margin-bottom: -300px;
`;

export const Main = styled.main`
  background-color: ${({ theme }) => theme.colors.bgContent};
  width: calc(100% - 25%);

  ::-webkit-scrollbar {
    display: none;
  }
`;

export const ContentWrapper = styled.main`
  max-width: 588px;
  padding-left: 40px;
`;
