import styled from 'styled-components';
import { Link } from 'react-router-dom';

const flexColumnPadding = `
display: flex; 
flex-direction: column;
padding-top: 3px;
`;

export const Wrapper = styled.div`
  padding: 0 50px;
`;

export const WrapperInputSingIn = styled.div`
  gap: 33px;
  margin-bottom: 25px;

  ${flexColumnPadding}
`;

export const WrapperInputSingUp = styled.div`
  gap: 25px;
  margin-bottom: 30px;

  ${flexColumnPadding}
`;

export const Extra = styled.div`
  display: -webkit-box;
  display: -ms-flexbox;
  display: flex;
  -webkit-box-pack: justify;
  -ms-flex-pack: justify;
  justify-content: space-between;
  margin-bottom: 44px;
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
