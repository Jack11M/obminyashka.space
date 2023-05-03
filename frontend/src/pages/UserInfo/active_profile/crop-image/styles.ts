/* eslint-disable indent */
import styled, { css } from 'styled-components';

export const WrapCropSvg = styled.div`
  position: absolute;

  > svg {
    opacity: 0;
    transition: opacity 0.2s ease;
  }
`;

export const WrapAvatar = styled.label<{ hasImage: boolean }>`
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

export const InputFile = styled.input`
  display: none;
`;
