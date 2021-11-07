import { useEffect, useState, useRef } from 'react';
import { useSelector } from 'react-redux';

import api from 'REST/Resources';
import { Items } from './items';
import { getTranslatedText } from 'components/local/localisation';

import {
  TitleH3,
  Sections,
  AddChoose,
  InputText,
  SectionsItem,
  ItemDescription,
} from './styles';
import { FormikCheckBox } from 'components/common/formik';

const SelectionSection = ({ category, subcategory, announcement }) => {
  const { lang } = useSelector((state) => state.auth);
  const [receivedCategories, setReceivedCategories] = useState([]);

  const refCategory = useRef();
  const refCloseCategory = useRef();
  const refSubCategory = useRef();
  const refCloseSubCategory = useRef();

  useEffect(() => {
    (async () => {
      const categories = await api.fetchAddGood.getCategoryAll();
      setReceivedCategories(categories);
    })();
  }, []);

  useEffect(() => {
    if (category.categoryItems) {
      subcategory.setSubCategoryItems('');
    }
  }, [category.categoryItems]);

  const getArrayKeys = (name) => {
    if (name) {
      return receivedCategories
        .find((item) => item.name === category.categoryItems)
        ?.subcategories.map((item) => item.name);
    }
    return receivedCategories.map((item) => item.name);
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
          <Items
            showImg
            data={getArrayKeys()}
            setItem={category.setCategoryItems}
            valueCategory={category.categoryItems}
            refClickAway={refCloseCategory}
            refHeightCategory={refCategory}
            placeholder={getTranslatedText(`addAdv.selectCategory`, lang)}
          />
        </SectionsItem>
        <SectionsItem>
          <ItemDescription>
            <span className="span_star">*</span>{' '}
            {getTranslatedText(`addAdv.subcategory`, lang)}
          </ItemDescription>
          <Items
            data={getArrayKeys('subcategories')}
            setItem={subcategory.setSubCategoryItems}
            valueCategory={subcategory.subCategoryItems}
            refClickAway={refCloseSubCategory}
            refHeightCategory={refSubCategory}
            placeholder={getTranslatedText(`addAdv.selectSubcategory`, lang)}
          />
        </SectionsItem>
        <SectionsItem>
          <ItemDescription>
            <span className="span_star">*</span>{' '}
            {getTranslatedText(`addAdv.headline`, lang)}
          </ItemDescription>
          <InputText
            value={announcement.announcementTitle}
            type="text"
            onChange={(e) => announcement.setAnnouncementTitle(e.target.value)}
          />
        </SectionsItem>
      </Sections>
      <FormikCheckBox
        type="checkbox"
        margin="22px 0 0 -7px"
        name="readyForOffers"
        text={getTranslatedText('addAdv.readyForOffers', lang)}
      />
    </AddChoose>
  );
};

export { SelectionSection };
