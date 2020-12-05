import React from "react";
import styled from 'styled-components';

const H2 = styled.h2`
  text-transform: uppercase;
  font-style: normal;
  font-weight: normal;
  font-size: 27px;
  line-height: 24px;
  font-family: Pollywog_Cyr, sans-serif;
  color: #11b5ed;
  &:before, 
  &:after{
    display: inline-block;
    content: "";
    width: 11px;
    height: 11px;
    border-radius: 50%;
    background-color: #11b5ed;
    margin-bottom: 5px;
  }
  &:before {
    margin-right: 27px;
  }
  &:after {
    margin-left: 27px;
  }
`

const TitleBigBlue = ({whatClass = '', text}) => {
  return (
    <React.Fragment>
      <H2 className={whatClass}>{text}</H2>
    </React.Fragment>
  );
};

export default TitleBigBlue;
