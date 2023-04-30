import styled from 'styled-components';

export const Overlay = styled.div`
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(18, 182, 237, 0.3);
  z-index: 100;
`;

export const Container = styled.div`
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  z-index: 1;
`;

export const Wrap = styled.div`
  position: relative;
  display: flex;
  flex-direction: column;
  width: 500px;
  padding: 24px;
  border-radius: 16px;
  overflow: hidden;

  background-color: rgba(9, 89, 114);
  box-shadow: 10px 10px 10px rgba(9, 89, 114, 0.4);
`;

export const Title = styled.p`
  font-size: 24px;
  line-height: 40px;
  color: ${({ theme }) => theme.colors.white};
`;

export const BlockCrop = styled.div`
  position: relative;
  margin: 24px 0;
  height: 290px;
  width: 100%;

  .reactEasyCrop_Container {
    border-radius: 8px;
  }
`;

export const RotationBlock = styled.div`
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 16px;
  margin-bottom: 24px;
  cursor: pointer;
  user-select: none;

  :hover {
    transform: scale(1.05);
  }
`;

export const Text = styled.p`
  font-size: 16px;
  color: #12b6ed;
`;

export const BlockButtons = styled.div`
  display: flex;
  flex-wrap: wrap;
  gap: 24px;

  > button {
    flex-grow: 1;
  }
`;
