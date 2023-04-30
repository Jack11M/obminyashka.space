import styled, { css } from 'styled-components';

const StyledError = styled.div<{ marginTop: string }>`
  width: fit-content;
  font-style: normal;
  font-weight: 400;
  line-height: 20px;

  ${({ theme, marginTop }) => css`
    margin-top: ${marginTop || '5px'};
    color: ${theme.colors.colorError};
  `}
`;

export { StyledError };
