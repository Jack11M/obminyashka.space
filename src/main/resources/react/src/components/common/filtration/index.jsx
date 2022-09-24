import { useEffect, useState } from 'react';
import { Form } from 'formik';

import api from 'REST/Resources';
import { showMessage } from 'hooks';
import { getTranslatedText, ua } from 'components/local';
import { FormHandler, FormikCheckBox } from 'components/common/formik';

import * as Styles from './styles';

import { Select } from './select';
import { CheckBoxes } from './checkbox';
import { InputsWithLocation } from './input';

const Filtration = () => {
  const [receivedCategories, setReceivedCategories] = useState([]);

  const [age, setAge] = useState([]);
  const [gender, setGender] = useState([]);
  const [season, setSeason] = useState([]);
  const [shoesSize, setShoesSize] = useState([]);
  const [clothesSize, setClothesSize] = useState([]);

  const { ageEnum, genderEnum, seasonEnum, clothingSizeEnum, shoesSizeEnum } =
    ua;

  const agesShow = Object.keys(ageEnum);
  const sexShow = Object.keys(genderEnum);
  const seasonShow = Object.keys(seasonEnum);
  const shoesSizeShow = Object.keys(shoesSizeEnum);
  const clothingSizeShow = Object.keys(clothingSizeEnum);

  useEffect(() => {
    (async () => {
      try {
        const categories = await api.fetchAddGood.getCategoryAll();
        setReceivedCategories(categories);
      } catch (err) {
        showMessage(err.response?.data ?? err.message);
      }
    })();
  }, []);

  return (
    <FormHandler>
      <Form>
        <Styles.CategoryFilter>
          <Styles.Title>
            {getTranslatedText('filterPage.categories')}
          </Styles.Title>

          <Styles.SelectBlock>
            {receivedCategories.map((category) => (
              <Select
                key={category.id}
                data={category.subcategories}
                title={getTranslatedText(`categories.${category.name}`)}
              />
            ))}
          </Styles.SelectBlock>
        </Styles.CategoryFilter>

        <Styles.Filter>
          <Styles.Title>{getTranslatedText('filterPage.filter')}</Styles.Title>

          <Styles.CheckBoxBlock>
            <InputsWithLocation />

            <CheckBoxes title={getTranslatedText('product.sex')}>
              {sexShow.map((item, idx) => (
                <FormikCheckBox
                  value={item}
                  type="checkbox"
                  fontSize="16px"
                  onChange={setGender}
                  selectedValues={gender}
                  key={String(item + idx)}
                  margin="4px 8px 4px 58px"
                  text={getTranslatedText(`genderEnum.${item}`)}
                />
              ))}
            </CheckBoxes>

            <CheckBoxes title={getTranslatedText('product.age')}>
              {agesShow.map((item, idx) => (
                <FormikCheckBox
                  value={item}
                  type="checkbox"
                  fontSize="16px"
                  onChange={setAge}
                  selectedValues={age}
                  key={String(item + idx)}
                  margin="4px 8px 4px 58px"
                  text={getTranslatedText(`ageEnum.${item}`)}
                />
              ))}
            </CheckBoxes>

            <CheckBoxes title={getTranslatedText('product.clothingSize')}>
              {clothingSizeShow.map((item, idx) => (
                <FormikCheckBox
                  value={item}
                  type="checkbox"
                  fontSize="16px"
                  key={String(item + idx)}
                  onChange={setClothesSize}
                  margin="4px 8px 4px 58px"
                  selectedValues={clothesSize}
                  text={getTranslatedText(`clothingSizeEnum.${item}`)}
                />
              ))}
            </CheckBoxes>

            <CheckBoxes title={getTranslatedText('product.shoeSize')}>
              {shoesSizeShow.map((item, idx) => (
                <FormikCheckBox
                  value={item}
                  fontSize="16px"
                  type="checkbox"
                  onChange={setShoesSize}
                  key={String(item + idx)}
                  margin="4px 8px 4px 58px"
                  selectedValues={shoesSize}
                  text={getTranslatedText(`shoesSizeEnum.${item}`)}
                />
              ))}
            </CheckBoxes>

            <CheckBoxes title={getTranslatedText('product.season')}>
              {seasonShow.map((item, idx) => (
                <FormikCheckBox
                  value={item}
                  fontSize="16px"
                  type="checkbox"
                  onChange={setSeason}
                  selectedValues={season}
                  key={String(item + idx)}
                  margin="4px 8px 4px 58px"
                  text={getTranslatedText(`seasonEnum.${item}`)}
                />
              ))}
            </CheckBoxes>
          </Styles.CheckBoxBlock>
        </Styles.Filter>
      </Form>
    </FormHandler>
  );
};

export { Filtration };
