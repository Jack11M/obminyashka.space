import styled from 'styled-components';

import { Button } from 'components/common';

export const MainContainer = styled.main`
  padding-top: 200px;
  padding-bottom: 50px;
`;

export const Container = styled.div`
  max-width: 1200px;
  margin: auto;
`;

export const AddContainer = styled.div`
  min-height: 600px;
  width: 100%;
  background-color: ${({ theme }) => theme.colors.white};
  padding: 40px;
`;

export const Wrap = styled.div`
  margin-bottom: 50px;
`;

export const TitleH3 = styled.h3`
  font-family: 'Open Sans', sans-serif;
  font-size: 26px;
  font-weight: 600;
  margin-bottom: 10px;
  line-height: 40px;
`;

export const WrapItems = styled.div`
  display: flex;
  justify-content: space-between;
  margin-bottom: 50px;
  flex-wrap: wrap;

  & :last-child {
    color: #383838;
    width: 350px;
  }
`;

export const SectionsItem = styled.div`
  max-width: 350px;
  width: 100%;
`;

export const ItemTittle = styled.h4`
  font-size: 22px;
  margin-bottom: 13px;
`;

export const WrapDescription = styled.div`
  padding-bottom: 40px;
`;

export const DescriptionText = styled.p`
  color: ${({ theme }) => theme.colors.colorGrey};
  margin-bottom: 10px;
`;

export const Star = styled.span`
  color: red;
`;

export const TextArea = styled.textarea`
  margin-top: 16px;
  padding: 10px;
  width: 100%;
  min-height: 150px;
  font-size: 16px;
  line-height: 24px;
  outline: none;
  resize: none;
  border: 1px solid #bcbcbc;
  border-radius: 2px;
  caret-color: ${({ theme }) => theme.colors.activeColor};
  &:focus {
    border-color: hsl(0, 0%, 44%);
  }
`;

export const WrapFiles = styled.div`
  margin-bottom: 100px;
`;

export const FileTittle = styled.h3`
  font-weight: 600;
  color: #11171f;
  font-size: 26px;
  line-height: 40px;
`;

export const FileDescription = styled.p`
  font-size: 16px;
  line-height: 26px;
  color: #8f8f8f;
  font-weight: 400;
`;

export const WrapperFile = styled.div`
  margin-top: 15px;
  display: grid;
  grid-template-columns: repeat(5, 1fr);
`;

export const WrapButtons = styled.div`
  display: flex;
  justify-content: space-between;
  align-items: center;
`;

export const BlockButtons = styled.div`
  display: flex;
  ${Button} {
    margin-left: 30px;
  }
`;

export const Cross = styled.div`
  position: relative;
  width: 15px;
  height: 15px;
  margin-right: 10px;
  transition: all 0.4s ease-in-out;

  &:before,
  &:after {
    position: absolute;
    content: '';
    width: 1px;
    height: 16px;
    background-color: black;
  }

  &::before {
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%) rotate(45deg);
  }

  &:after {
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%) rotate(-45deg);
  }
`;

export const BackButtons = styled.div`
  display: flex;
  padding: 5px;
  text-transform: uppercase;
  cursor: pointer;
  transition: all 0.2s ease-in-out;

  &:hover {
    ${Cross} {
      transform: rotate(180deg);
    }
  }

  &:active {
    transform: scale(1.15);
  }
`;
