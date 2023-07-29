import styled from "styled-components";

export const Title = styled.div`
  display: flex;
  justify-content: center;
  align-items: center;
  width: 50%;
  border-bottom: 2px solid #dfdfdf;

  &.active {
    border-bottom: 2px solid #29a5d4;
  }
`;

export const Button = styled.button`
  width: 100%;
  height: 100%;
`;
