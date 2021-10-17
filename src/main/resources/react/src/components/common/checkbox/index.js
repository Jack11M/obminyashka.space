import React from 'react';

import { ReactComponent as CheckSvg } from 'assets/icons/Check.svg';

import { Div, LabelSquare, Label } from './styles.js';

const CheckBox = ({
  margin,
  click = null,
  checked = false,
  distanceBetween = false,
  fontSize,
  text,
}) => {
  return (
    <Div margin={margin} onClick={click} checked={checked}>
      <LabelSquare checked={checked}>
        <CheckSvg />
      </LabelSquare>
      <div>
        <Label
          checked={checked}
          fontSize={fontSize}
          distanceBetween={distanceBetween}
        >
          {text}
        </Label>
      </div>
    </Div>
  );
};

export default CheckBox;
