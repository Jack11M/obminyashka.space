/* eslint-disable indent */
import styled, { css } from 'styled-components';

export const AddRemoveItem = styled.div`
  display: inline-flex;
  align-items: center;
  gap: 20px;

  ${({ add }) => css`
    ${add
      ? css`
          margin-left: 135px;
        `
      : css`
          position: absolute;
          top: 3px;
          right: -68px;
        `}
  `}
`;

export const Span = styled.span`
  position: relative;
  display: inline-flex;
  width: 34px;
  height: 34px;
  color: #fff;
  border-radius: 50%;
  transition: 0.2s;
  font-size: 14px;
  line-height: 16px;
  margin-right: 21px;

  ${({ add }) => css`
    background-color: ${add ? 'hsl(134, 45%, 48%)' : 'hsl(0, 0%, 47%)'};

    &:hover {
      cursor: pointer;
      background-color: ${add ? 'hsl(134, 45%, 43%)' : 'hsl(0, 0%, 42%)'};
    }

    :before {
      position: absolute;
      content: '';
      top: 16px;
      left: 12px;
      width: 10px;
      height: 2px;
      background-color: #fff;
      transform: ${add ? 'rotate(0deg)' : 'rotate(45deg)'};
    }

    :after {
      position: absolute;
      content: '';
      top: 12px;
      left: 16px;
      width: 2px;
      height: 10px;
      background-color: #fff;
      transform: ${add ? 'rotate(0deg)' : 'rotate(45deg)'};
    }
  `}
`;
