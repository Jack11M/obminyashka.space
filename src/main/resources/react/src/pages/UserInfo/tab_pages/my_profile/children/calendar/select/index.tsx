import { useEffect, useRef, useState } from 'react';

import { useOutsideClick } from 'src/hooks/useOutsideClick';

import * as Styles from './styles';

const Select = ({ selectedValue, data, changeMonth, changeYear }) => {
  const refClickAway = useRef();
  const [open, setOpen] = useState(false);
  const [selected, setSelected] = useState(selectedValue);

  useEffect(() => {
    if (selectedValue !== selected) {
      setSelected(selectedValue);
    }
  }, [selectedValue]);

  useOutsideClick(refClickAway, () => {
    setOpen(false);
  });

  return (
    <Styles.Container>
      <Styles.Element ref={refClickAway} onClick={() => setOpen(!open)}>
        {selected}
      </Styles.Element>

      <Styles.Elements>
        {open &&
          data?.map((value) => (
            <Styles.Span
              key={value}
              selected={value === selected}
              onClick={() => {
                setOpen(false);
                setSelected(value);
                if (changeMonth) {
                  changeMonth(data.indexOf(value));
                }
                if (changeYear) {
                  changeYear(value);
                }
              }}
            >
              {value}
            </Styles.Span>
          ))}
      </Styles.Elements>
    </Styles.Container>
  );
};

export { Select };
