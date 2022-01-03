import { useField } from 'formik';
import { ErrorDisplay } from 'pages/AddGoods/error-display';
import { useEffect, useState } from 'react';
import { useSelector } from 'react-redux';

import api from 'REST/Resources';
import { FormikCheckBox } from 'components/common/formik';
import { getTranslatedText } from 'components/local/localisation';

import { SelectItem } from './select-item';

import {
  TitleH3,
  Sections,
  AddChoose,
  InputText,
  SectionsItem,
  ItemDescription,
} from './styles';

const SelectionSection = ({
  category,
  subcategory,
  readyOffers,
  announcement,
}) => {
  const { lang } = useSelector((state) => state.auth);

  const [currLang, setCurrLang] = useState(lang);
  const [tempCategory, setTempCategory] = useState(category.categoryItems);
  const [receivedCategories, setReceivedCategories] = useState([]);
  const [, meta, helpers] = useField({ name: 'topic' });
  const { error, touched } = meta;

  useEffect(() => {
    helpers.setError(undefined);
  }, [lang]);

  useEffect(() => {
    (async () => {
      try {
        const categories = await api.fetchAddGood.getCategoryAll();
        if (Array.isArray(categories)) {
          setReceivedCategories(categories);
        } else {
          throw { message: 'OOps, I didn’t get the category' };
        }
      } catch (err) {
        console.log(err.response?.data ?? err.message);
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
    <AddChoose>
      <TitleH3 className="add-title">
        {getTranslatedText(`addAdv.chooseSection`, lang)}
      </TitleH3>

      <Sections>
        <SectionsItem>
          <ItemDescription>
            <span className="span_star">*</span>{' '}
            {getTranslatedText(`addAdv.category`, lang)}
          </ItemDescription>

          <SelectItem
            showImg
            name="categoryId"
            data={getArrayKeys()}
            setItem={category.setCategoryItems}
            valueCategory={category.categoryItems}
            placeholder={getTranslatedText(`addAdv.selectCategory`, lang)}
          />
        </SectionsItem>

        <SectionsItem>
          <ItemDescription>
            <span className="span_star">*</span>{' '}
            {getTranslatedText(`addAdv.subcategory`, lang)}
          </ItemDescription>

          <SelectItem
            name="subcategoryId"
            data={getArrayKeys('subcategories')}
            setItem={subcategory.setSubCategoryItems}
            valueCategory={subcategory.subCategoryItems}
            placeholder={getTranslatedText(`addAdv.selectSubcategory`, lang)}
          />
        </SectionsItem>

        <SectionsItem>
          <ItemDescription>
            <span className="span_star">*</span>{' '}
            {getTranslatedText(`addAdv.headline`, lang)}
          </ItemDescription>

          <InputText
            type="text"
            error={touched && !!error}
            value={announcement.announcementTitle}
            onChange={(e) => announcement.setAnnouncementTitle(e.target.value)}
          />

          <ErrorDisplay error={touched && error} />
        </SectionsItem>
      </Sections>

      <FormikCheckBox
        type="checkbox"
        margin="22px 0 0 -7px"
        name="readyForOffers"
        value="readyForOffers"
        onChange={readyOffers.setReadyOffer}
        selectedValues={readyOffers.readyOffer}
        text={getTranslatedText('addAdv.readyForOffers', lang)}
      />
    </AddChoose>
  );
};

export { SelectionSection };
