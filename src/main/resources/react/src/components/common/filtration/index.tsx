import { useMemo, useEffect, useState } from 'react';
import { Form } from 'formik';
import { useSelector } from 'react-redux';
import { showMessage } from 'obminyashka-components';

import api from 'src/REST/Resources';
import ua from 'src/components/local/ua';
import { enumAge } from 'src/config/ENUM';
import { getAuthLang } from 'src/store/auth/slice';
import { getTranslatedText } from 'src/components/local';
import { FormikHandler, FormikCheckBox } from 'src/components/common/formik';

import * as Styles from './styles';

import { Select } from './select';
import { CheckBoxes } from './checkbox';
import { InputsWithLocation } from './input';

const Filtration = () => {
  const lang = useSelector(getAuthLang);

  const [receivedShoeSizes, setReceivedShoeSize] = useState([]);
  const [receivedCategories, setReceivedCategories] = useState([]);
  const [receivedClothingSizes, setReceivedClothingSize] = useState([]);

  const [age, setAge] = useState([]);
  const [gender, setGender] = useState([]);
  const [season, setSeason] = useState([]);
  const [shoesSizes, setShoesSizes] = useState([]);
  const [clothesSizes, setClothesSizes] = useState([]);

  const { seasonEnum, genderEnum } = ua;

  const agesShow = Object.keys(enumAge);
  const sexShow = Object.keys(genderEnum);
  const seasonShow = Object.keys(seasonEnum);

  useEffect(() => {
    (async () => {
      try {
        const [categories, clothingSizes, shoeSizes] = await Promise.all([
          api.addGood.getCategoryAll(),
          api.addGood.getSize(1),
          api.addGood.getSize(2),
        ]);

        if (Array.isArray(categories) && Array.isArray(clothingSizes) && Array.isArray(shoeSizes)) {
          setReceivedClothingSize(clothingSizes);
          setReceivedShoeSize(shoeSizes);
          setReceivedCategories(categories);
        }
      } catch (err: any) {
        showMessage.error(err.response?.data ?? err?.message);
      }
    })();
  }, []);

  const currentType = useMemo(() => {
    return lang === 'en' ? 'cm' : 'см';
  }, [lang]);

  return (
    <FormikHandler>
      <Form>
        <Styles.CategoryFilter>
          <Styles.Title>{getTranslatedText('filterPage.categories')}</Styles.Title>

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
              {sexShow.map((item) => (
                <FormikCheckBox
                  gap={20}
                  key={item}
                  value={item}
                  fontSize={16}
                  type='checkbox'
                  onChange={setGender}
                  selectedValues={gender}
                  margin='4px 8px 4px 25px'
                  text={getTranslatedText(`genderEnum.${item}`)}
                />
              ))}
            </CheckBoxes>

            <CheckBoxes title={getTranslatedText('product.age')}>
              {agesShow.map((item) => (
                <FormikCheckBox
                  gap={20}
                  key={item}
                  value={item}
                  fontSize={16}
                  type='checkbox'
                  onChange={setAge}
                  selectedValues={age}
                  text={enumAge[item]}
                  margin='4px 8px 4px 25px'
                />
              ))}
            </CheckBoxes>

            <CheckBoxes title={getTranslatedText('product.clothingSizes')}>
              <Styles.ScrollBar>
                {receivedClothingSizes.map((item) => (
                  <FormikCheckBox
                    gap={20}
                    key={item}
                    value={item}
                    fontSize={16}
                    type='checkbox'
                    margin='4px 8px 4px 25px'
                    onChange={setClothesSizes}
                    selectedValues={clothesSizes}
                    text={`${item} ${currentType}`}
                  />
                ))}
              </Styles.ScrollBar>
            </CheckBoxes>

            <CheckBoxes title={getTranslatedText('product.shoeSizes')}>
              <Styles.ScrollBar>
                {receivedShoeSizes.map((item) => (
                  <FormikCheckBox
                    gap={20}
                    key={item}
                    value={item}
                    fontSize={16}
                    type='checkbox'
                    onChange={setShoesSizes}
                    margin='4px 8px 4px 25px'
                    selectedValues={shoesSizes}
                    text={`${item} ${currentType}`}
                  />
                ))}
              </Styles.ScrollBar>
            </CheckBoxes>

            <CheckBoxes title={getTranslatedText('product.season')}>
              {seasonShow.map((item) => (
                <FormikCheckBox
                  gap={20}
                  key={item}
                  value={item}
                  fontSize={16}
                  type='checkbox'
                  onChange={setSeason}
                  selectedValues={season}
                  margin='4px 8px 4px 25px'
                  text={getTranslatedText(`seasonEnum.${item}`)}
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
