import styled, { css } from 'styled-components';

export const Container = styled.div`
  position: relative;
  display: flex;
  width: 100px;
  flex-shrink: 0;
  align-items: center;
`;

export const Element = styled.div`
  padding: 8px;
  width: 100%;
  font-size: 18px;
  line-height: 24px;
  border: 1px solid #12b6ed;
  border-radius: 8px;
  cursor: pointer;
  user-select: none;
`;

export const Elements = styled.div`
  position: absolute;
  top: 42px;
  left: 0;
  display: flex;
  flex-direction: column;
  width: 100%;
  background-color: #fff;
  font-size: 14px;
  box-shadow: 0px 0px 38px -24px rgba(0, 0, 0, 1.25),
    0px 31px 32px -24px rgba(0, 0, 0, 0.18);
  overflow: hidden;
  border-radius: 8px;
  z-index: 1;
`;

export const Span = styled.span`
  padding: 6px;
  cursor: pointer;

  ${({ selected }) => css`
    ${selected &&
    css`
      background-color: #12b6ed;
      color: #fff;
    `}

    :hover {
      background-color: ${selected ? '#12b6ed ' : '#12b6ed1a'};
    }
  `}
`;
