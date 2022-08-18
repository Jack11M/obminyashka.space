import { useEffect, useState } from 'react';
import { useSelector } from 'react-redux';

import api from 'REST/Resources';
import { getAuthLang } from 'store/auth/slice';
import { getTranslatedText } from 'components/local/localization';

import { ShowSelectItem } from '../show-select-item';

import * as Styles from '../styles';

const Sizes = ({ categories, dimension }) => {
  const lang = useSelector(getAuthLang);

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
    <Styles.WrapItem>
      <Styles.ItemTittle>
        {getTranslatedText('addAdv.size')}
        {lang === 'en' ? ' cm' : ' см'}
        {translatedText &&
          ` (${getTranslatedText(`categories.${translatedText}`)})`}
      </Styles.ItemTittle>

      <ShowSelectItem
        name="size"
        overflows
        data={receivedSizes}
        text={dimension.size}
        value={dimension.size}
        onClick={dimension.setSize}
        typeError="popup.selectSize"
        titleError="popup.errorTitleSize"
        placeholder={getTranslatedText('addAdv.chooseSize')}
      />
    </Styles.WrapItem>
  );
};

export { Sizes };
