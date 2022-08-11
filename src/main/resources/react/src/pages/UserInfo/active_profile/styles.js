import styled from 'styled-components';

export const ProfileBlock = styled.div`
  padding-top: 44px;
  width: 100%;
  height: 243px;
  background: ${({ theme }) => theme.colors.btnBlue};
`;

export const ProfileBox = styled.div`
  display: flex;
  flex-direction: column;
  align-items: flex-end;
  margin-right: 36px;
`;

export const BoxData = styled.div`
  display: flex;
  justify-content: center;
  margin-top: 16px;
  margin-right: -10px;
  width: 162px;
`;

export const DataName = styled.div`
  position: relative;
  display: flex;
  justify-content: center;
  width: 100%;
  font-size: 14px;
  line-height: 16px;
  color: ${({ theme }) => theme.colors.white};
`;
