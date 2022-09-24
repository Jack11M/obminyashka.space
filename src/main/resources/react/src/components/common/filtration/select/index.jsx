import { useState } from 'react';

import * as Icon from 'assets/icons';
import { getTranslatedText } from 'components/local';

import * as Styles from './styles';

const Select = ({ title, data }) => {
  const [select, setSelect] = useState(
    data.map((item) => ({ ...item, isSelected: false }))
  );

  const [openSelect, setOpenSelect] = useState(false);

  const handleSelected = (id) => {
    const arrData = select.map((item) => {
      if (item.id === id) {
        return {
          ...item,
          isSelected: !item.isSelected,
        };
      }
      return item;
    });
    setSelect(arrData);
  };

  return (
    <div>
      <Styles.TitleBlock onClick={() => setOpenSelect(!openSelect)}>
        <Styles.TitleBlockWrapper>
          <Styles.Title>{title}</Styles.Title>

          <Styles.RotateRectangle openSelect={openSelect}>
            <Icon.Rectangle />
          </Styles.RotateRectangle>
        </Styles.TitleBlockWrapper>
      </Styles.TitleBlock>

      <Styles.OptionWrapper hideSelect={openSelect}>
        {openSelect &&
          select?.map((subcategory) => (
            <Styles.SubTitleBlock
              key={subcategory.id}
              isSelected={subcategory.isSelected}
              onClick={() => handleSelected(subcategory.id)}
            >
              <span>{getTranslatedText(`categories.${subcategory.name}`)}</span>

              <Styles.Close isSelected={subcategory.isSelected}>
                <Icon.CloseSvg />
              </Styles.Close>
            </Styles.SubTitleBlock>
          ))}
      </Styles.OptionWrapper>
    </div>
  );
};

export { Select };
