import React from 'react';

import SpinnerForAuthBtn from 'components/common/spinner/spinnerForAuthBtn.js';

import { ButtonBlue } from './styles.js'

const Button = ({
  whatClass = null,
  type,
  mb,
  bold,
  lHeight,
  disabling = null,
  text,
  click = null,
  width,
  isLoading
}) => {
  return (
    <ButtonBlue
      className={whatClass}
      type={type}
      disabled={disabling}
      onClick={click}
      width={width}
      mb={mb}
      bold={bold}
      lHeight={lHeight}
    >
      {isLoading ? <SpinnerForAuthBtn /> : text}
    </ButtonBlue>
  );
};
export default React.memo(Button);
