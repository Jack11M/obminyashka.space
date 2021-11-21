import React from 'react';
import { useSelector } from 'react-redux';

import { getTranslatedText } from 'components/local/localisation';

import { ShowSelectItem } from '../../show-select-item';

const SelectItem = ({ data, showImg, setItem, placeholder, valueCategory }) => {
  const { lang } = useSelector((state) => state.auth);

  return (
    <ShowSelectItem
      categories
      data={data}
      showImg={showImg}
      onClick={setItem}
      placeholder={placeholder}
      value={valueCategory.name}
      typeError="popup.selectCategory"
      text={getTranslatedText(`categories.${valueCategory.name}`, lang)}
    />
  );
};

export { SelectItem };
