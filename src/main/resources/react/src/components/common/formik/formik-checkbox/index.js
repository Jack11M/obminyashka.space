import React from 'react';
import { useField } from 'formik';

import { ReactComponent as CheckSvg } from 'assets/icons/Check.svg';

import { Div, LabelSquare, Label, Input } from './styles.js';

const FormikCheckBox = ({
  text,
  type,
  margin,
  fontSize,
  distanceBetween = false,
  ...props
}) => {
  const [field] = useField(props);
  return (
    <Div margin={margin}>
      <Label fontSize={fontSize}>
        <Input type={type} {...field} {...props} />
        <LabelSquare>
          <CheckSvg />
        </LabelSquare>
        <span>{text}</span>
      </Label>
    </Div>
  );
};

export { FormikCheckBox };
