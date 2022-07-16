import styled, { css } from 'styled-components';
import SomethingBad from 'assets/img/errorPage/SomethingBad.png';

export const WrapCenter = styled.div`
  position: relative;
  width: 596px;
  margin: 278px auto 73px;
`;

export const WrapImg404 = styled(WrapCenter)`
  position: relative;
  z-index: 10;
`;

export const WrapperShadow = styled(WrapCenter)`
  position: absolute;
  left: calc(50% - (483px / 2));
  bottom: -72px;
  display: flex;
  align-items: center;
  justify-content: center;
  width: 483px;
  background-color: #e2e2e2;
  z-index: 0;
  img {
    display: inline-flex;
  }
`;

export const WrapperLight = styled(WrapperShadow)`
  width: 483px;
  position: relative;
`;

export const WrapperDark = styled(WrapperShadow)`
  width: 178px;
  position: absolute;
`;
export const WrapImg = styled.div`
  position: absolute;
  top: 0;
  z-index: 0;
  img {
    max-width: 100%;
    height: calc(100vh - 3px);
  }
`;

export const WrapOImg = styled(WrapImg)`
  left: 360px;
`;
export const WrapGImg = styled(WrapImg)`
  left: 0;
`;
export const WrapRImg = styled(WrapImg)`
  right: 50px;
`;

export const Tittle = styled.h2`
  font-family: 'Pollywog Cyr', sans-serif;
  font-style: normal;
  font-weight: normal;
  font-size: 27px;
  line-height: 24px;
  letter-spacing: 1px;
  text-transform: uppercase;
  color: #bababa;
  text-align: center;
  margin-bottom: 27px;
`;

export const Container = styled.div`
  min-height: 100vh;
  position: relative;
  background: url(${SomethingBad});
  background-repeat: no-repeat;
  background-size: cover;
  background-position: center;
  @media (max-width: 576px) {
    background-size: 300%;
    background-position: top;
    background-position-y: 15%;
  }
`;

export const WrapOrange = styled(WrapImg)`
  left: 360px;
  z-index: -1;
`;
export const WrapGreen = styled(WrapImg)`
  left: 0;
  z-index: -1;
`;
export const WrapRight = styled(WrapImg)`
  right: 50px;
  z-index: -1;
`;

export const WrapTittleBad = styled.div`
  position: absolute;
  left: 0;
  right: 0;
  bottom: 10%;
`;

export const TittleBad = styled(Tittle)`
  padding: 10px 5px;
  @media (max-width: 576px) {
    font-size: 16px;
  }
`;

export const WrapperButton = styled.div`
  position: relative;
  display: flex;
  flex-wrap: wrap;
  justify-content: space-around;
  max-width: 480px;
  margin: 0 auto 50px;
`;

export const button = css`
  margin: 0 5px 20px;
  border-radius: 24px;
  border: 0;
  outline: none;
  width: 222px;
  height: 50px;
  font-family: 'Roboto', sans-serif;
  font-style: normal;
  font-weight: normal;
  font-size: 16px;
  line-height: 19px;
  text-transform: uppercase;
  color: #ffffff;
`;

export const MainButton = styled.button`
  ${button};
  ${({ theme }) => css`
    background-color: ${theme.colors['btn-blue-normal']};
    &:hover {
      cursor: pointer;
      background-color: ${theme.colors['btn-blue-hover']};
    }
    &:active {
      background-color: ${theme.colors['btn-blue-active']};
    }
    &:disabled {
      background-color: ${theme.colors['btn-gb-disabled']};
    }
  `}
`;

export const BackButton = styled.button`
  ${button};
  ${({ theme }) => css`
    background-color: ${theme.colors['btn-green-normal']};
    &:hover {
      cursor: pointer;
      background-color: ${theme.colors['btn-green-hover']};
    }
    &:active {
      background-color: ${theme.colors['btn-green-active']};
    }
    &:disabled {
      background-color: ${theme.colors['btn-gb-disabled']};
    }
  `}
`;
