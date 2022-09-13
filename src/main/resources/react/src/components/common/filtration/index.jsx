import { getTranslatedText, ua } from 'components/local';

import { Select } from './select';
import { CheckBoxes } from './checkbox';
import { LocationInputs } from './input';

import * as Styles from './styles';

const Filtration = () => {
  const { ageEnum, genderEnum, seasonEnum, clothingSizeEnum, shoesSizeEnum } =
    ua;

  const agesShow = Object.keys(ageEnum);
  const sexShow = Object.keys(genderEnum);
  const seasonShow = Object.keys(seasonEnum);
  const clothingSizeShow = Object.keys(clothingSizeEnum);
  const shoesSizeShow = Object.keys(shoesSizeEnum);

  return (
    <>
      <Styles.CategoryFilter>
        <Styles.Title>
          {getTranslatedText('filterPage.categories')}
        </Styles.Title>

        <Styles.SelectBlock>
          <Select />
        </Styles.SelectBlock>
      </Styles.CategoryFilter>

      <Styles.Filter>
        <Styles.Title>{getTranslatedText('filterPage.filter')}</Styles.Title>

        <Styles.CheckBoxBlock>
          <LocationInputs />

          <CheckBoxes title="Пол" data={sexShow} dataText="genderEnum" />

          <CheckBoxes title="Возраст" data={agesShow} dataText="ageEnum" />

          <CheckBoxes
            title="Размер (Одежда)"
            data={clothingSizeShow}
            dataText="clothingSizeEnum"
          />

          <CheckBoxes
            data={shoesSizeShow}
            title="Размер (Обувь)"
            dataText="shoesSizeEnum"
          />

          <CheckBoxes title="Сезон" data={seasonShow} dataText="seasonEnum" />
        </Styles.CheckBoxBlock>
      </Styles.Filter>
    </>
  );
};

export { Filtration };
