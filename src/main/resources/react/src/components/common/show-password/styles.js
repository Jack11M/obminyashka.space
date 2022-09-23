import styled from 'styled-components';

export const WrapIcon = styled.div`
  position: absolute;
  display: flex;
  top: ${({ distance }) => distance || '34px'};
  right: 10px;
  cursor: pointer;
`;
