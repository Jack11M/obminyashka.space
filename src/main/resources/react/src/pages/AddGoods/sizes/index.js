import React, { useEffect, useState } from 'react';
import { useSelector } from 'react-redux';

import api from 'REST/Resources';
import { getTranslatedText } from 'components/local/localisation';

import { ShowSelectItem } from '../show-select-item';

const Sizes = ({ categories, dimension }) => {
  const { lang } = useSelector((state) => state.auth);

  const [receivedSizes, setReceivedSizes] = useState(null);
  const [translatedText, setTranslatedText] = useState('');

  useEffect(() => {
    if (categories.id === 1 || categories.id === 2) {
      dimension.setSize('');
      api.fetchAddGood
        .getSize(`${categories.id}`)
        .then((data) => {
          setReceivedSizes(data);
          setTranslatedText(categories.id === 1 ? 'Clothing' : 'Shoes');
        })
        .catch((err) => err.response?.data ?? err.message);
    } else {
      setReceivedSizes(null);
      dimension.setSize('');
    }
  }, [categories]);

  return (
    <div className="characteristics_item">
      <h4>
        {getTranslatedText('addAdv.size', lang)}
        {translatedText &&
          ` (${getTranslatedText(`categories.${translatedText}`, lang)})`}
      </h4>
      <ShowSelectItem
        overflows
        data={receivedSizes}
        text={dimension.size}
        value={dimension.size}
        onClick={dimension.setSize}
        placeholder="Выберите размер"
        typeError="popup.selectSize"
        titleError="popup.errorTitleSize"
      />
    </div>
  );
};

export { Sizes };
