import styled, { css } from 'styled-components';
import { Link } from 'react-router-dom';

import footerBg from 'assets/img/footerBg.png';

export const Container = styled.div`
  position: relative;
  margin-top: auto;
  background-image: url(${footerBg});
  background-size: cover;
  background-position: top;
  background-repeat: no-repeat;
  z-index: 2;

  ${({ theme }) => css`
    color: ${theme.colors.white};
  `}

  @media screen and (min-width: 800px) {
    padding-top: 100px;
  }
`;

export const Wrapper = styled.div`
  width: 100%;
  max-width: 1251px;
  padding: 0 15px;
  margin: 0 auto;
  overflow: hidden;
`;

export const Blocks = styled.ul`
  display: flex;
  flex-direction: column;

  @media screen and (min-width: 800px) {
    flex-direction: row;
    justify-content: space-between;
    height: 220px;
    padding-bottom: 50px;
  }
`;

export const Lists = styled.li`
  align-items: center;
  display: flex;
  flex-direction: column;
  padding: 10px;
`;

export const Span = styled.span`
  display: inline-block;
  width: 25px;
  height: 25px;
  margin-bottom: 44px;
`;

export const WrapContacts = styled.div`
  padding-top: 20px;
`;

export const Contact = styled.a`
  font-size: 22px;
  line-height: 30px;

  display: block;
  line-height: 24px;
  text-decoration: none;
`;

export const FootLink = styled(Link)`
  display: block;
  line-height: 24px;
  text-decoration: none;
`;

export const FootLinkWrapper = styled.div`
  margin-bottom: ${({ rules }) => (rules ? '10px' : '0')};
`;

export const Img = styled.img`
  :active {
    transform: scale(1.03);
  }
`;

export const BlockButton = styled.div`
  margin-top: 40px;
  background-color: ${({ theme }) => theme.colors.btnBlue};
  padding: 1px 16px;
  text-transform: uppercase;
  letter-spacing: 1px;
  font-weight: lighter;
  border-radius: 25px;
  max-width: 100%;
  white-space: nowrap;

  &:hover {
    background-color: ${({ theme }) => theme.colors.btnBlueActive};
  }

  span {
    margin-right: 10px;
  }

  @media screen and (min-width: 800px) {
    margin-top: 20px;
  }
`;

export const CopyContainer = styled.div`
  text-align: center;
  font-size: 14px;
  font-weight: 300;
  color: rgba(255, 255, 255, 0.5);
  padding: 20px 0;
`;

export const SpanCopy = styled.span`
  padding-right: 10px;
  font-size: 16px;
`;
