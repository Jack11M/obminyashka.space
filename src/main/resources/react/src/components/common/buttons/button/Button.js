import React from 'react';

import SpinnerForAuthBtn from 'components/common/spinner/spinnerForAuthBtn.js';

import { ButtonBlue, WrapIcon } from './styles.js';

const Button = ({
  mb,
  type,
  bold,
  icon,
  text,
  width,
  lHeight,
  isLoading,
  click = null,
  whatClass = null,
  disabling = null,
}) => {
  return (
    <ButtonBlue
      mb={mb}
      type={type}
      bold={bold}
      width={width}
      onClick={click}
      lHeight={lHeight}
      disabled={disabling}
      className={whatClass}
    >
      {isLoading ? <SpinnerForAuthBtn /> : text}
      {icon && <WrapIcon>{icon}</WrapIcon>}
    </ButtonBlue>
  );
};
export default React.memo(Button);
