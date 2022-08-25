import { useField } from 'formik';
import { useEffect, useState } from 'react';
import { useSelector } from 'react-redux';

import api from 'REST/Resources';
import { showMessage } from 'hooks';
import { getAuthLang } from 'store/auth/slice';
import { ErrorDisplay } from 'pages/AddGoods/error-display';
import { getTranslatedText } from 'components/local/localization';

import * as Styles from './styles';
import { SelectItem } from './select-item';

import {
  TitleH3,
  Sections,
  AddChoose,
  InputText,
  SectionsItem,
  ItemDescription,
} from './styles';

const SelectionSection = ({ category, subcategory, announcement }) => {
  const lang = useSelector(getAuthLang);

  const [currLang, setCurrLang] = useState(lang);
  const [tempCategory, setTempCategory] = useState(category.categoryItems);
  const [receivedCategories, setReceivedCategories] = useState([]);
  const [, meta] = useField({ name: 'topic' });
  const { error, touched } = meta;

  useEffect(() => {
    (async () => {
      try {
        const categories = await api.fetchAddGood.getCategoryAll();
        if (Array.isArray(categories)) {
          setReceivedCategories(categories);
        } else {
          // eslint-disable-next-line no-throw-literal
          throw { message: 'OOps, I didn’t get the category' };
        }
      } catch (err) {
        showMessage(err.response?.data ?? err.message);
      }
    })();
  }, []);

  useEffect(() => {
    if (currLang !== lang) {
      if (category.categoryItems) {
        subcategory.setSubCategoryItems('');
      }
      setCurrLang(lang);
    }

    if (tempCategory !== category.categoryItems) {
      subcategory.setSubCategoryItems('');
      setTempCategory(category.categoryItems);
    }
  }, [category.categoryItems, lang, tempCategory]);

  const getArrayKeys = (name) => {
    if (name) {
      return receivedCategories
        .find((item) => item?.name === category.categoryItems.name)
        ?.subcategories.map((item) => ({ name: item?.name, id: item?.id }));
    }

    return receivedCategories.map((item) => ({
      id: item?.id,
      name: item?.name,
    }));
  };

  return (
    <Styles.AddChoose>
      <Styles.TitleH3>
        {getTranslatedText('addAdv.chooseSection')}
        <Styles.Star>*</Styles.Star>
      </Styles.TitleH3>

      <Styles.Sections>
        <Styles.SectionsItem>
          <Styles.ItemDescription>
            {getTranslatedText('addAdv.category')}
          </Styles.ItemDescription>

          <SelectItem
            showImg
            name="categoryId"
            data={getArrayKeys()}
            setItem={category.setCategoryItems}
            valueCategory={category.categoryItems}
            placeholder={getTranslatedText('addAdv.selectCategory')}
          />
        </Styles.SectionsItem>

        <Styles.SectionsItem>
          <Styles.ItemDescription>
            {getTranslatedText('addAdv.subcategory')}
          </Styles.ItemDescription>

          <SelectItem
            name="subcategoryId"
            data={getArrayKeys('subcategories')}
            setItem={subcategory.setSubCategoryItems}
            valueCategory={subcategory.subCategoryItems}
            placeholder={getTranslatedText('addAdv.selectSubcategory')}
          />
        </Styles.SectionsItem>

        <Styles.SectionsItem>
          <Styles.ItemDescription>
            {getTranslatedText('addAdv.headline')}
          </Styles.ItemDescription>

          <Styles.InputText
            type="text"
            error={touched && !!error}
            value={announcement.announcementTitle}
            onChange={(e) => announcement.setAnnouncementTitle(e.target.value)}
          />

          <ErrorDisplay error={touched && error} />
        </SectionsItem>
      </Sections>
    </AddChoose>
  );
};

export { SelectionSection };
