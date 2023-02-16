import { useCallback } from 'react';

import { CheckSvg } from 'assets/icons';

import * as Styles from './styles';

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
      const filtered = selectedValues.filter((selected) => selected === item);

      return filtered.length > 0;
    },
    [selectedValues]
  );

  return (
    <Styles.Div margin={margin}>
      <Styles.Label fontSize={fontSize}>
        <Styles.Input
          type={type}
          name={name}
          value={value}
          checked={isSelected(value)}
          onChange={() => handleChange(value)}
        />
        <Styles.LabelSquare type={type}>
          <CheckSvg />
        </Styles.LabelSquare>
        <span>{text}</span>
      </Styles.Label>
    </Styles.Div>
  );
};

export { FormikCheckBox };
