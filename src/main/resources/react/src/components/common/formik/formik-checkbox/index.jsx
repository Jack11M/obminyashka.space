import { useCallback } from 'react';

import { CheckBox } from '@wolshebnik/obminyashka-components';

import { Div } from './styles';

export const FormikCheckBox = ({
  gap,
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
      const filtered = selectedValues.filter((selected) => selected === item);

      return filtered.length > 0;
    },
    [selectedValues]
  );

  return (
    <Div>
      <CheckBox
        gap={gap}
        type={type}
        text={text}
        name={name}
        margin={margin}
        value={value}
        fontSize={fontSize}
        checked={isSelected(value)}
        onChange={() => handleChange(value)}
      />
    </Div>
  );
};
