import React from 'react';
import { useSelector } from 'react-redux';

import { getLang } from 'store/auth/slice';
import { getTranslatedText } from 'components/local/localization';

import { ShowSelectItem } from '../../show-select-item';

const SelectItem = ({
  name,
  data,
  showImg,
  setItem,
  placeholder,
  valueCategory,
}) => {
  const lang = useSelector(getLang);

  return (
    <ShowSelectItem
      categories
      name={name}
      data={data}
      showImg={showImg}
      onClick={setItem}
      placeholder={placeholder}
      value={valueCategory.name}
      typeError="popup.selectCategory"
      titleError="popup.errorTitleSubCategory"
      text={getTranslatedText(`categories.${valueCategory.name}`, lang)}
    />
  );
};

export { SelectItem };
