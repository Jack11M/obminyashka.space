import styled from 'styled-components';

const StyledError = styled.div`
  width: fit-content;
  margin-top: ${(props) => (props.marginTop ? props.marginTop : '5px')};
  font-style: normal;
  font-weight: 400;
  line-height: 20px;
  color: ${({ theme }) => theme.colors.colorError};
`;

export { StyledError };
