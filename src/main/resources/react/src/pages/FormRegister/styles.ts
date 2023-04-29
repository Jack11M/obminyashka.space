import styled from 'styled-components';

export const Container = styled.div`
  display: flex;
  flex-direction: column;
  margin: 180px auto 50px;
  background-color: ${({ theme }) => theme.colors.colorPopup};
  max-width: 806px;
  width: 100%;
  border-radius: 1px;
  -webkit-box-shadow: 0 0 24px 10px rgba(48, 50, 50, 0.3);
  -moz-box-shadow: 0 0 24px 10px rgba(48, 50, 50, 0.3);
  box-shadow: 0 0 24px 10px rgba(48, 50, 50, 0.3);
`;
