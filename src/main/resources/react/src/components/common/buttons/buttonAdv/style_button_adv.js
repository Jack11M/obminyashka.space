import styled from 'styled-components';
import { Link } from 'react-router-dom';

export const ButtonAdd = styled(Link)`
  position: relative;
  display: block;
  width: 290px;
  color: #fff;
  background-color: ${({ theme: { colors } }) => colors.btnGreen};
  border-radius: 25px;
  padding: 13px 35px 13px 61px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  @media (min-width: 320px) {
    display: none;
  }
  &:before {
    position: absolute;
    content: '';
    top: 23px;
    left: 33px;
    width: 14px;
    height: 2px;
    background-color: #fff;
  }
  &:after {
    position: absolute;
    content: '';
    top: 17px;
    left: 39px;
    width: 2px;
    height: 14px;
    background-color: #fff;
  }
  &:hover,
  &:active {
    background-color: #008b20;
  }
`;
export const Span = styled.span`
  font-size: 16px;
  line-height: 24px;
  text-transform: uppercase;
`;
