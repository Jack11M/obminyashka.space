import { useMemo, useEffect, useState } from 'react';
import { Form } from 'formik';
import { useSelector } from 'react-redux';

import api from 'REST/Resources';
import { showMessage } from 'hooks';
import ua from 'components/local/ua';
import { enumAge } from 'config/ENUM';
import { getAuthLang } from 'store/auth/slice';
import { getTranslatedText } from 'components/local';
import { FormikHandler, FormikCheckBox } from 'components/common/formik';

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
        const categories = await api.addGood.getCategoryAll();
        setReceivedCategories(categories);
      } catch (err) {
        showMessage(err.response?.data ?? err.message);
      }
    })();
  }, []);

  useEffect(() => {
    (async () => {
      try {
        const clothingSizes = await api.addGood.getSize(1);
        const shoeSizes = await api.addGood.getSize(2);
        setReceivedClothingSize(clothingSizes);
        setReceivedShoeSize(shoeSizes);
      } catch (err) {
        showMessage(err.response?.data ?? err.message);
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
              {sexShow.map((item) => (
                <FormikCheckBox
                  key={item}
                  value={item}
                  type="checkbox"
                  fontSize="16px"
                  onChange={setGender}
                  selectedValues={gender}
                  margin="4px 8px 4px 58px"
                  text={getTranslatedText(`genderEnum.${item}`)}
                />
              ))}
            </CheckBoxes>

            <CheckBoxes title={getTranslatedText('product.age')}>
              {agesShow.map((item) => (
                <FormikCheckBox
                  key={item}
                  value={item}
                  type="checkbox"
                  fontSize="16px"
                  onChange={setAge}
                  selectedValues={age}
                  text={enumAge[item]}
                  margin="4px 8px 4px 58px"
                />
              ))}
            </CheckBoxes>

            <CheckBoxes title={getTranslatedText('product.clothingSizes')}>
              <Styles.ScrollBar>
                {receivedClothingSizes.map((item) => (
                  <FormikCheckBox
                    key={item}
                    value={item}
                    type="checkbox"
                    fontSize="16px"
                    margin="4px 8px 4px 58px"
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
                    key={item}
                    value={item}
                    fontSize="16px"
                    type="checkbox"
                    onChange={setShoesSizes}
                    margin="4px 8px 4px 58px"
                    selectedValues={shoesSizes}
                    text={`${item} ${currentType}`}
                  />
                ))}
              </Styles.ScrollBar>
            </CheckBoxes>

            <CheckBoxes title={getTranslatedText('product.season')}>
              {seasonShow.map((item) => (
                <FormikCheckBox
                  key={item}
                  value={item}
                  fontSize="16px"
                  type="checkbox"
                  onChange={setSeason}
                  selectedValues={season}
                  margin="4px 8px 4px 58px"
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
