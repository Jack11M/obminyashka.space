import React, { useCallback } from 'react';

import { ReactComponent as CheckSvg } from 'assets/icons/Check.svg';

import { Div, LabelSquare, Label, Input } from './styles.js';

const FormikCheckBox = ({
  text,
  type,
  name,
  value,
  margin,
  fontSize,
  onChange,
  selectedValues,
}) => {
  const handleChange = (item) => {
    if (type === 'checkbox') {
      const findIndex = selectedValues.findIndex((i) => i === item);

      if (findIndex === -1) {
        onChange([...selectedValues, item]);
      } else {
        onChange([...selectedValues.filter((i) => i !== item)]);
      }
    } else {
      onChange([item]);
    }
  };
  const isSelected = useCallback(
    (item) => {
      const filtered = selectedValues.filter((selected) => {
        return selected === item;
      });

      return filtered.length > 0;
    },
    [selectedValues]
  );

  return (
    <Div margin={margin}>
      <Label fontSize={fontSize}>
        <Input
          type={type}
          name={name}
          value={value}
          checked={isSelected(value)}
          onChange={() => handleChange(value)}
        />
        <LabelSquare type={type}>
          <CheckSvg />
        </LabelSquare>
        <span>{text}</span>
      </Label>
    </Div>
  );
};

export { FormikCheckBox };
