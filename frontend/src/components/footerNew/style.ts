import styled, { css } from "styled-components";

export const Wrapper = styled.div<{ withBg: boolean }>`
  ${({ withBg }) => css`
    ${withBg &&
    css`
      background: linear-gradient(22deg, #97d7e3 18.83%, #39a5cf 100%);
    `}
  `}
`;
