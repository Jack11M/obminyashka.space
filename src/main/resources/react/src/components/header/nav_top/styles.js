import styled from 'styled-components';
import { Link } from 'react-router-dom';

export const Div = styled.div`
  background-color: #f2f4f7;
  font-family: Roboto, sans-serif;
`;

export const Wrapper = styled.div``;

export const DivTop = styled.div`
  display: flex;
  justify-content: space-between;
  font-size: 16px;
  line-height: 16px;

  align-items: center;
`;

export const WrapLinks = styled.div`
  color: ${({ theme: { colors } }) => colors.blackColorText};
  text-transform: uppercase;
`;

export const NavTopLink = styled(Link)`
  margin-right: 60px;
  font-weight: 300;
  text-transform: uppercase;
  cursor: pointer;

  & > svg {
    position: relative;
    top: 2px;
    margin-right: 8px;
  }
`;

export const WrapPersonal = styled.div`
  display: flex;
  min-width: 295px;
  justify-content: space-between;
`;

export const LoginLink = styled(Link)`
  position: relative;
  transition: 0.2s;
  display: flex;
  align-items: center;
  padding: 16px 0 10px;
  cursor: pointer;
  & > div {
    width: 30px;
  }
`;

export const ProfileSpan = styled.span`
  margin-left: 10px;
  line-height: 16px;
  color: #444444;
  display: block;
  width: 150px;
  overflow: hidden;
  white-space: nowrap;
  text-overflow: ellipsis;

  &:active {
    transform: scale(1.03);
  }
`;
