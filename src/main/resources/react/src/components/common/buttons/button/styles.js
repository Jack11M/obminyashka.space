import styled, { css } from 'styled-components';

 export const ButtonBlue = styled.button`
  background-color: ${({ theme: { colors } }) => colors['btn-blue-normal']};
  border-radius: 25px;
  border: 0;
  outline: none;
  width: ${({ width }) => width};
  height: 49px;
  font-family: 'Roboto', sans-serif;
  font-style: normal;
  font-weight: ${(props) => (props.bold ? 'bold' : 'normal')};
  font-size: 16px;
  line-height: ${(props) => (props.lHeight ? props.lHeight : '20px')};
  text-transform: uppercase;
  transition: background-color 0.3s ease;
  ${({ mb }) =>
	mb &&
	css`
      margin-bottom: ${mb};
    `}
  color: #fdf9ff;

  &:hover {
    cursor: pointer;
    background-color: ${({ theme: { colors } }) => colors['btn-blue-hover']};
  }

  &:active {
    cursor: pointer;
    background-color: ${({ theme: { colors } }) => colors['btn-blue-active']};
  }

  &:disabled {
    background-color: ${({ theme: { colors } }) => colors['btn-gb-disabled']};
  }
`;
