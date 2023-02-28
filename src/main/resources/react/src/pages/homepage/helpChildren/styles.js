import styled from 'styled-components';
import childrenImg from 'assets/img/children.png';

export const HelpChildren = styled.div`
  display: block;
  border-radius: 20px;
  margin-bottom: 145px;
  padding: 45px 45px 60px;
  background: #fff url(${childrenImg}) no-repeat bottom right;

  @media (min-width: 320px) {
    display: none;
  }

  @media (min-width: 980px) {
    display: block;
  }
`;

export const HelpChildrenTitleH3 = styled.h3`
  color: #12b6ed;
  font-size: 50px;
  line-height: 24px;
  font-family: Caveat;
  padding-bottom: 36px;
`;

export const HelpChildrenText = styled.p`
  color: #111;
  width: 408px;
  font-size: 18px;
  line-height: 24px;
  font-family: Roboto;
  border-radius: 20px;
  margin-bottom: 40px;
  backdrop-filter: blur(3px);
  background-color: rgba(255, 255, 255, 0.4);
  box-shadow: 0 0 20px rgba(255, 255, 255, 0.2);

  @media (min-width: 980px) {
    width: 250px;
  }

  @media (min-width: 1140px) {
    width: 408px;
  }
`;

export const Strong = styled.strong`
  color: #00c0ff;
  font-weight: 700;
  display: contents;
  line-height: 15px;
  text-decoration: none;
`;

export const StylizedBtn = styled.div`
  color: #fff;
  font-size: 18px;
  transition: 0.2s;
  padding: 16px 50px;
  border-radius: 30px;
  display: inline-block;
  text-decoration: none;
  text-transform: uppercase;
  background-color: #00c0ff;

  span {
    margin-right: 10px;
  }

  :hover {
    background-color: #0094ff;
  }
`;

export const TitleWrapper = styled.div`
  padding-bottom: 36px;
`;
