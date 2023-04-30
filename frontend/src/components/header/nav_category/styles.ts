import styled from 'styled-components';
import { Link } from 'react-router-dom';

export const List = styled.ul`
  background-color: #fff;
  padding: 70px;
  color: ${({ theme }) => theme.colors.colorGrey};
  z-index: -1;
  transition: 1s all;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.08);
  width: 100vw;
  left: 0;
  top: 138px;
  position: fixed;
`;

export const Wrapper = styled.div`
  padding: 20px 0;
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  grid-auto-rows: 157px;
  grid-row-gap: 20px;
  width: 100%;
`;

export const NavbarLinkContainer = styled.div`
  display: grid;
  justify-content: center;
  align-items: center;
`;

export const NavbarLink = styled(Link)`
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  width: 157px;
  height: 157px;

  &:hover {
    border: 2px dashed #ffb641;
    box-sizing: border-box;
    box-shadow: 0 4px 40px rgba(255, 199, 0, 0.4);
    border-radius: 111px;
  }
`;

export const Img = styled.img`
  display: block;
`;

export const Span = styled.span`
  display: block;
  margin-top: 20px;
  font-size: 14px;
  font-style: normal;
  font-weight: 600;
  line-height: 17px;
  text-align: center;
  color: ${({ theme }) => theme.colors.rightColorText};
  text-transform: uppercase;
`;
