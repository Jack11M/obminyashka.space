import styled from 'styled-components';
import { Link } from 'react-router-dom';

export const Extra = styled.div`
  display: -webkit-box;
  display: -ms-flexbox;
  display: flex;
  -webkit-box-pack: justify;
  -ms-flex-pack: justify;
  justify-content: space-between;
`;

export const ExtraLink = styled(Link)`
  font-style: normal;
  font-weight: 600;
  font-size: 14px;
  line-height: 17px;
  text-decoration-line: none;
  color: ${({ theme }) => theme.colors.btnBlue};
`;

export const WrapperButton = styled.div`
  display: flex;
  justify-content: space-between;
`;

export const Form = styled.form`
  margin: 0 50px 0 45px;
`;
