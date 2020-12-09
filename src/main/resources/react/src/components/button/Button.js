import React, { Fragment } from 'react';
import styled from 'styled-components';

const ButtonBlue = styled.button`
  background-color: ${ ({ theme: { colors } }) => colors['btn-blue-normal']};
  border-radius: 25px;
  border: 0;
  outline: none;
  width: ${({ width }) => width};
  height: 49px;

  font-family:  'Roboto', sans-serif;
  font-style: normal;
  font-weight: ${(props)=>props.bold ? 'bold': 'normal' };
  font-size: 16px;
  line-height: 20px;
  text-transform: uppercase;
  transition: background-color 0.3s ease;

  color: #fdf9ff;

 &:hover {
    cursor: pointer;
    background-color: ${ ({ theme: { colors } }) => colors['btn-blue-hover']};
  }

  &:active {
    cursor: pointer;
    background-color: ${ ({ theme: { colors } }) => colors['btn-blue-active']};
  }

  &:disabled {
    background-color: ${ ({ theme: { colors } }) => colors['btn-gb-disabled']};
  }
`


const Button = ({ whatClass = null, disabling, text, click = null, width}) => {
  return (
    <Fragment>
      <ButtonBlue
        className={whatClass}
        disabled={disabling || null}
        onClick={click}
        width={width}
      >
        {text}
      </ButtonBlue>
    </Fragment>
  );
};
export default Button;
