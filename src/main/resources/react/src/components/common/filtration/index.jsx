import { useEffect, useState } from 'react';
import { Form } from 'formik';

import {
  enumAge,
  enumSex,
  seasonEnum,
  shoesSizeEnum,
  clothingSizeEnum,
} from 'config/ENUM';
import api from 'REST/Resources';
import { showMessage } from 'hooks';
import { getTranslatedText } from 'components/local';
import { FormikHandler, FormikCheckBox } from 'components/common/formik';

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

  const sexShow = Object.keys(enumSex);
  const agesShow = Object.keys(enumAge);
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
    <FormikHandler>
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
                  text={sexShow[item]}
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
                  text={agesShow[item]}
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
                  text={clothingSizeShow[item]}
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
                  text={shoesSizeShow[item]}
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
                  text={seasonShow[item]}
                />
              ))}
            </CheckBoxes>
          </Styles.CheckBoxBlock>
        </Styles.Filter>
      </Form>
    </FormikHandler>
  );
};

export { Filtration };
