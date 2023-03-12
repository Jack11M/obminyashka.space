import styled from 'styled-components';
import { Link } from 'react-router-dom';

export const InputContainer = styled.div`
  display: flex;
  flex-direction: column;
  gap: 20px;
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
  color: #12b6ed;
  text-decoration: none;
`;

export const InputWrapper = styled.div`
  display: flex;
  flex-direction: column;
  gap: 35px;
  margin: 25px 0 50px;
`;

export const ModalTitle = styled.p`
  font-size: 20px;
  line-height: 16px;
  font-weight: 600;
  font-style: normal;
  margin-bottom: 25px;
  color: ${({ theme }) => theme.colors.colorError};
`;
