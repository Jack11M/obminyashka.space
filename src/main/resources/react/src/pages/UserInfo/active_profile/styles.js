/* eslint-disable indent */
import styled, { css } from 'styled-components';

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

export const WrapCropSvg = styled.div`
  position: absolute;

  > svg {
    opacity: 0;
    transition: opacity 0.2s ease;
  }
`;

export const WrapAvatar = styled.div`
  position: relative;
  display: flex;
  justify-content: center;
  align-items: center;
  width: 135px;
  height: 135px;
  background-color: rgba(0, 0, 0, 0.3);
  border-radius: 50%;
  cursor: pointer;
  overflow: hidden;
  transition: background-color 0.3s ease;

  path {
    transition: fill 0.3s ease;
  }

  svg:nth-child(2) {
    opacity: 1;
  }

  :hover {
    background-color: #cccccc;

    > svg:first-child {
      path {
        fill: #095972;
      }
    }

    ${({ hasImage }) =>
      hasImage &&
      css`
        opacity: 0.8;
      `}

    ${WrapCropSvg} {
      > svg {
        opacity: 1;
      }
    }
  }
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
