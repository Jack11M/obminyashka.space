import styled from 'styled-components';

export const H4 = styled.h4`
  color: ${(p) => (p.error ? p.theme.colors.colorError : '#383838')};
`;
