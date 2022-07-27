import styled from 'styled-components';
import { Link } from 'react-router-dom';

export const InputContainer = styled.div`
  display: block;
  width: 100%;
  height: auto;
`;

export const ButtonContainer = styled.div`
  margin-bottom: 17px;
  margin-top: 40px;
`;

export const WarningText = styled.p`
  display: block;
  width: 599px;
  height: 75px;
  font-size: 14px;
  line-height: 23px;
`;

export const StylizedLink = styled(Link)`
  text-decoration: none;
  color: #12b6ed;
`;
