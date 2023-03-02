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
  display: flex;
  justify-content: center;
  width: 162px;
  color: ${({ theme }) => theme.colors.white};
  font-size: 14px;
  line-height: 16px;
  z-index: 1;
`;

export const ImageCrop = styled.img`
  position: absolute;
  display: flex;
  justify-content: center;
  align-items: center;
  object-fit: scale-down;
  top: 50%;
  left: 50%;
  width: 40px;
  height: 40px;
  border-radius: 50%;
  overflow: hidden;
  transform: translate(-50%, -50%);
`;
