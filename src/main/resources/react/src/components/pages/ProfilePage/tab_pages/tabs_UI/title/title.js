import React from "react";
import styled from "styled-components";
import "../../../../../../fonts/font.scss";

const Title = props => {
  const Div = styled.div`
    margin-bottom: 40px;
  `;

  const H2 = styled.h2`
    text-transform: uppercase;
    font-style: normal;
    font-weight: normal;
    font-size: 27px;
    line-height: 24px;
    color: #11b5ed;
    font-family: Pollywog_Cyr, san-serif;
    :before,
    &:after {
      display: inline-block;
      content: "";
      width: 11px;
      height: 11px;
      border-radius: 50%;
      background-color: #11b5ed;
      margin-bottom: 5px;
    }
    :before {
      margin-right: 27px;
    }
    :after {
      margin-left: 27px;
    }
  `;

  return (
    <Div>
      <H2>{props.text}</H2>
    </Div>
  );
};

export default Title;
