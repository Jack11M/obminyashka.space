import { Link } from 'react-router-dom';
import styled, { css } from 'styled-components';

export const StyledLink = styled(Link)`
  padding-top: 14px;
  padding-bottom: 10px;
  width: 50%;

  text-decoration: none;
  list-style-type: none;
  text-align: center;
  font-style: normal;
  font-weight: normal;
  font-size: 16px;
  line-height: 24px;
  text-transform: uppercase;
  border-bottom: 2px solid #8e8e8e;
  color: #8e8e8e;
  cursor: pointer;

  &.active-link {
    border-bottom: 2px solid #00c0ff;
    font-weight: bold;
    color: #00c0ff;
  }
`;
