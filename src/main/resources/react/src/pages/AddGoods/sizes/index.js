import React, { useEffect, useState } from 'react';
import { useSelector } from 'react-redux';

import api from 'REST/Resources';
import { getTranslatedText } from 'components/local/localisation';

import { ShowSelectItem } from '../show-select-item';

const Sizes = ({ categories, dimension }) => {
  const { lang } = useSelector((state) => state.auth);

  const [tempId, setTempId] = useState(categories.id);
  const [receivedSizes, setReceivedSizes] = useState(null);
  const [translatedText, setTranslatedText] = useState('');

  const getText = (id) => {
    switch (id) {
      case 1:
        return 'Clothing';
      case 2:
        return 'Shoes';
      default:
        return '';
    }
  };

  useEffect(() => {
    if (categories.id === 1 || categories.id === 2) {
      if (tempId !== categories.id) {
        dimension.setSize('');
        setTempId(categories.id);
      }

      api.fetchAddGood
        .getSize(categories.id)
        .then((data) => {
          setReceivedSizes(data);
        })
        .catch((err) => err.response?.data ?? err.message);
    } else {
      setReceivedSizes(null);
      dimension.setSize('');
    }

    setTranslatedText(getText(categories.id));
  }, [categories]);

  return (
    <div className="characteristics_item">
      <h4>
        {getTranslatedText('addAdv.size', lang)}
        {lang === 'en' ? ' cm' : ' см'}
        {translatedText &&
          ` (${getTranslatedText(`categories.${translatedText}`, lang)})`}
      </h4>

      <ShowSelectItem
        name="size"
        overflows
        data={receivedSizes}
        text={dimension.size}
        value={dimension.size}
        onClick={dimension.setSize}
        typeError="popup.selectSize"
        placeholder="Выберите размер"
        titleError="popup.errorTitleSize"
      />
    </div>
  );
};

export { Sizes };
