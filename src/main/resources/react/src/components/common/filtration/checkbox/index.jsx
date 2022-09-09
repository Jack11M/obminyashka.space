import { useState } from 'react';

import * as Icon from 'assets/icons';

import * as Styles from './styles';

const CheckBoxes = ({ categoryTitle, subCategory }) => {
  const [openSelect, setOpenSelect] = useState(false);

  const [options, setOptions] = useState(subCategory);

  const handleSelected = (id) => {
    const data = options.map((option) => {
      if (option.id === id) {
        return {
          ...option,
          isSelected: !option.isSelected,
        };
      }
      return option;
    });
    setOptions(data);
  };

  return (
    <>
      <Styles.TitleBlock onClick={() => setOpenSelect(!openSelect)}>
        <Styles.Title>{categoryTitle}</Styles.Title>
        <Styles.RotateRectangle openSelect={openSelect}>
          <Icon.Rectangle />
        </Styles.RotateRectangle>
      </Styles.TitleBlock>

      {openSelect && (
        <Styles.OptionWrapper>
          {options.map((option) => (
            <Styles.SubTitleBlock
              key={option.id}
              isSelected={option.isSelected}
              onClick={() => handleSelected(option.id)}
            >
              <span>{option.subTitle}</span>
            </Styles.SubTitleBlock>
          ))}
        </Styles.OptionWrapper>
      )}
    </>
  );
};

export { CheckBoxes };
