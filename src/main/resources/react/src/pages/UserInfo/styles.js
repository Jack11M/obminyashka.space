import styled from 'styled-components';

export const Container = styled.div`
  display: flex;
  margin: 156px 0 auto 0;
  width: 100%;
  font-family: Roboto;
`;

export const Aside = styled.aside`
  display: flex;
  flex-direction: column;
  align-items: flex-end;
  width: 25%;
  height: auto;
  background-color: #ffffff;
  color: darken(rgba(173, 173, 173, 0.78), 5%);
  margin-bottom: -300px;
`;

export const Main = styled.main`
  background-color: #e5e5e5;
  width: calc(100% - 25%);

  ::-webkit-scrollbar {
    display: none;
  }
`;

export const ContentWrapper = styled.main`
  max-width: 588px;
  padding-left: 40px;
`;
