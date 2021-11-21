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

const SelectionSection = ({ category, subcategory, announcement }) => {
  const { lang } = useSelector((state) => state.auth);
  const [receivedCategories, setReceivedCategories] = useState([]);

  useEffect(() => {
    (async () => {
      try {
        const categories = await api.fetchAddGood.getCategoryAll();
        if (Array.isArray(categories)) {
          setReceivedCategories(categories);
        } else {
          throw { message: 'OOps I didn’t get the category' };
        }
      } catch (err) {
        console.log(err.response?.data ?? err.message);
      }
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
