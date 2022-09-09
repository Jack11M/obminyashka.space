import { useState } from 'react';

import { CheckBox } from 'components/common';
import { getTranslatedText } from 'components/local';

import { Select } from './select';
import { CheckBoxes } from './checkbox';
// import { select, checkbox } from './config';

import * as Styles from './styles';

const Filtration = () => {
  const [checkbox, setCheckbox] = useState(false);

  const changeCheckBox = () => {
    setCheckbox((prev) => !prev);
  };

  const subOptions1 = [
    { id: 1, subTitle: 'Блузки, рубашки', isSelected: false },
    { id: 2, subTitle: 'Брюки, джинсы, шорты', isSelected: false },
    { id: 3, subTitle: 'Верхняя одежда', isSelected: false },
    {
      id: 4,
      subTitle: 'Головные уборы, шарфы, варежки, перчатки',
      isSelected: false,
    },
    { id: 5, subTitle: 'Платья , сарафаны, юбки', isSelected: false },
    { id: 6, subTitle: 'Свитора, кофты', isSelected: false },
    { id: 7, subTitle: 'Футболки, топы, майки', isSelected: false },
    { id: 8, subTitle: 'Спортивные костюмы', isSelected: false },
    { id: 9, subTitle: 'Школьная форма', isSelected: false },
  ];

  const subOptions2 = [
    { id: 1, subTitle: 'Кросовки, кеды', isSelected: false },
    { id: 2, subTitle: 'Ботинки, сапоги', isSelected: false },
    { id: 3, subTitle: 'Босоножки, тапочки', isSelected: false },
    {
      id: 4,
      subTitle: 'Туфли, макасины',
      isSelected: false,
    },
    {
      id: 5,
      subTitle: 'Другое',
      isSelected: false,
    },
  ];

  const subOptions3 = [
    { id: 1, subTitle: 'Игрушки для малышей', isSelected: false },
    { id: 2, subTitle: 'Машинки и техника', isSelected: false },
    { id: 3, subTitle: 'Мягкие игрушки', isSelected: false },
    {
      id: 4,
      subTitle: 'Конструкторы',
      isSelected: false,
    },
    {
      id: 5,
      subTitle: 'Куклы, фигурки',
      isSelected: false,
    },
  ];

  const subOptions4 = [
    { id: 1, subTitle: 'Детские кроватки', isSelected: false },
    { id: 2, subTitle: 'Двухярусные кровати', isSelected: false },
    { id: 3, subTitle: 'Манежи', isSelected: false },
    {
      id: 4,
      subTitle: 'Столы, стулья',
      isSelected: false,
    },
    {
      id: 5,
      subTitle: 'Стульчик для кормления',
      isSelected: false,
    },
    {
      id: 6,
      subTitle: 'Авто кресло',
      isSelected: false,
    },
  ];

  const subOptions5 = [
    { id: 1, subTitle: 'Коляски', isSelected: false },
    { id: 2, subTitle: 'Самокаты, велосипеды', isSelected: false },
    { id: 3, subTitle: 'Машинки', isSelected: false },
  ];

  const subOptions6 = [
    { id: 1, subTitle: 'Книги-игрушки', isSelected: false },
    { id: 2, subTitle: 'Позновательные книги', isSelected: false },
    { id: 3, subTitle: 'Художественная литература', isSelected: false },
    { id: 4, subTitle: 'Школьные книги', isSelected: false },
    { id: 5, subTitle: 'Энциклопедии', isSelected: false },
  ];

  const subOptions7 = [
    { id: 1, subTitle: 'Животные (зайчик, мишка, лисичка)', isSelected: false },
    {
      id: 2,
      subTitle: 'Профессии (Повар, пожарник, полицейский)',
      isSelected: false,
    },
    { id: 3, subTitle: 'Мультперсонажи', isSelected: false },
    { id: 4, subTitle: 'Другое', isSelected: false },
  ];

  const subOptions8 = [{ id: 1, subTitle: 'Другое', isSelected: false }];

  const subOptions9 = [
    {
      id: 1,
      subTitle: <Styles.Input type="text" placeholder="Область" />,
    },
    {
      id: 2,
      subTitle: <Styles.Input type="text" placeholder="Город" />,
    },
  ];

  const subOptions10 = [
    {
      id: 1,
      subTitle: (
        <CheckBox
          margin="0"
          text="Девочка"
          fontSize="16px"
          checked={checkbox}
          click={changeCheckBox}
        />
      ),
      isSelected: false,
    },
    {
      id: 2,
      subTitle: (
        <CheckBox
          margin="0"
          text="Мальчик"
          fontSize="16px"
          checked={checkbox}
          click={changeCheckBox}
        />
      ),
      isSelected: false,
    },
    {
      id: 3,
      subTitle: (
        <CheckBox
          fontSize="16px"
          margin="0"
          checked={checkbox}
          click={changeCheckBox}
          text="Подойдет всем"
        />
      ),
      isSelected: false,
    },
  ];

  const subOptions11 = [
    {
      id: 1,
      subTitle: (
        <CheckBox
          fontSize="16px"
          margin="0"
          checked={checkbox}
          click={changeCheckBox}
          text="0"
        />
      ),
      isSelected: false,
    },
    {
      id: 2,
      subTitle: (
        <CheckBox
          fontSize="16px"
          margin="0"
          checked={checkbox}
          click={changeCheckBox}
          text="1 - 2"
        />
      ),
      isSelected: false,
    },
    {
      id: 3,
      subTitle: (
        <CheckBox
          fontSize="16px"
          margin="0"
          checked={checkbox}
          click={changeCheckBox}
          text="2 - 4"
        />
      ),
      isSelected: false,
    },
    {
      id: 4,
      subTitle: (
        <CheckBox
          fontSize="16px"
          margin="0"
          checked={checkbox}
          click={changeCheckBox}
          text="5 - 7"
        />
      ),
      isSelected: false,
    },
    {
      id: 5,
      subTitle: (
        <CheckBox
          fontSize="16px"
          margin="0"
          checked={checkbox}
          click={changeCheckBox}
          text="8 - 11"
        />
      ),
      isSelected: false,
    },
    {
      id: 6,
      subTitle: (
        <CheckBox
          fontSize="16px"
          margin="0"
          checked={checkbox}
          click={changeCheckBox}
          text="11 - 14"
        />
      ),
      isSelected: false,
    },
  ];

  const subOptions12 = [
    {
      id: 1,
      subTitle: (
        <CheckBox
          fontSize="16px"
          margin="0"
          checked={checkbox}
          click={changeCheckBox}
          text="50 - 80 см"
        />
      ),
      isSelected: false,
    },
    {
      id: 2,
      subTitle: (
        <CheckBox
          fontSize="16px"
          margin="0"
          checked={checkbox}
          click={changeCheckBox}
          text="80 - 92 см"
        />
      ),
      isSelected: false,
    },
    {
      id: 3,
      subTitle: (
        <CheckBox
          fontSize="16px"
          margin="0"
          checked={checkbox}
          click={changeCheckBox}
          text="92 - 110 см"
        />
      ),
      isSelected: false,
    },
    {
      id: 4,
      subTitle: (
        <CheckBox
          fontSize="16px"
          margin="0"
          checked={checkbox}
          click={changeCheckBox}
          text="110 - 122 см"
        />
      ),
      isSelected: false,
    },
    {
      id: 5,
      subTitle: (
        <CheckBox
          fontSize="16px"
          margin="0"
          checked={checkbox}
          click={changeCheckBox}
          text="122 - 146 см"
        />
      ),
      isSelected: false,
    },
    {
      id: 6,
      subTitle: (
        <CheckBox
          fontSize="16px"
          margin="0"
          checked={checkbox}
          click={changeCheckBox}
          text="146 - 164"
        />
      ),
      isSelected: false,
    },
  ];

  const subOptions13 = [
    {
      id: 7,
      subTitle: (
        <CheckBox
          fontSize="16px"
          margin="0"
          checked={checkbox}
          click={changeCheckBox}
          text="9,5 см - 16"
        />
      ),
      isSelected: false,
    },
    {
      id: 8,
      subTitle: (
        <CheckBox
          fontSize="16px"
          margin="0"
          checked={checkbox}
          click={changeCheckBox}
          text="10 см - 16,5"
        />
      ),
      isSelected: false,
    },
    {
      id: 9,
      subTitle: (
        <CheckBox
          fontSize="16px"
          margin="0"
          checked={checkbox}
          click={changeCheckBox}
          text="10,5 см - 17"
        />
      ),
      isSelected: false,
    },
    {
      id: 10,
      subTitle: (
        <CheckBox
          fontSize="16px"
          margin="0"
          checked={checkbox}
          click={changeCheckBox}
          text="11 см - 18"
        />
      ),
      isSelected: false,
    },
    {
      id: 11,
      subTitle: (
        <CheckBox
          fontSize="16px"
          margin="0"
          checked={checkbox}
          click={changeCheckBox}
          text="11,5 см - 19"
        />
      ),
      isSelected: false,
    },
    {
      id: 12,
      subTitle: (
        <CheckBox
          fontSize="16px"
          margin="0"
          checked={checkbox}
          click={changeCheckBox}
          text="12 см - 20"
        />
      ),
      isSelected: false,
    },
    {
      id: 13,
      subTitle: (
        <CheckBox
          fontSize="16px"
          margin="0"
          checked={checkbox}
          click={changeCheckBox}
          text="12,5 см - 21"
        />
      ),
      isSelected: false,
    },
    {
      id: 14,
      subTitle: (
        <CheckBox
          fontSize="16px"
          margin="0"
          checked={checkbox}
          click={changeCheckBox}
          text="13 см - 22"
        />
      ),
      isSelected: false,
    },
    {
      id: 15,
      subTitle: (
        <CheckBox
          fontSize="16px"
          margin="0"
          checked={checkbox}
          click={changeCheckBox}
          text="13,5 см - 23"
        />
      ),
      isSelected: false,
    },
    {
      id: 16,
      subTitle: (
        <CheckBox
          fontSize="16px"
          margin="0"
          checked={checkbox}
          click={changeCheckBox}
          text="14 см - 24"
        />
      ),
      isSelected: false,
    },
  ];

  const subOptions14 = [
    {
      id: 1,
      subTitle: (
        <CheckBox
          fontSize="16px"
          margin="0"
          checked={checkbox}
          click={changeCheckBox}
          text="Демисезон"
        />
      ),
      isSelected: false,
    },
    {
      id: 2,
      subTitle: (
        <CheckBox
          fontSize="16px"
          margin="0"
          checked={checkbox}
          click={changeCheckBox}
          text="Лето"
        />
      ),
      isSelected: false,
    },
    {
      id: 3,
      subTitle: (
        <CheckBox
          fontSize="16px"
          margin="0"
          checked={checkbox}
          click={changeCheckBox}
          text="Зима"
        />
      ),
      isSelected: false,
    },
  ];

  const select = [
    {
      categoryTitle: getTranslatedText('products.clothes'),
      subCategory: subOptions1,
    },
    {
      categoryTitle: getTranslatedText('products.shoes'),
      subCategory: subOptions2,
    },
    {
      categoryTitle: getTranslatedText('products.toys'),
      subCategory: subOptions3,
    },
    {
      categoryTitle: getTranslatedText('products.children_furniture'),
      subCategory: subOptions4,
    },
    {
      categoryTitle: getTranslatedText('products.transport_for_children'),
      subCategory: subOptions5,
    },
    {
      categoryTitle: getTranslatedText('products.books'),
      subCategory: subOptions6,
    },
    {
      categoryTitle: getTranslatedText('products.carnival_costumes'),
      subCategory: subOptions7,
    },
    {
      categoryTitle: getTranslatedText('products.other'),
      subCategory: subOptions8,
    },
  ];

  const checkBox = [
    {
      categoryTitle: 'Локация',
      subCategory: subOptions9,
    },
    {
      categoryTitle: 'Пол',
      subCategory: subOptions10,
    },
    {
      categoryTitle: 'Возраст',
      subCategory: subOptions11,
    },
    {
      categoryTitle: 'Размер (Одежда)',
      subCategory: subOptions12,
    },
    {
      categoryTitle: 'Размер (Обувь)',
      subCategory: subOptions13,
    },
    {
      categoryTitle: 'Сезон',
      subCategory: subOptions14,
    },
  ];

  return (
    <>
      <Styles.CategoryFilter>
        <Styles.Title>
          {getTranslatedText('filterPage.categories')}
        </Styles.Title>

        <Styles.SelectBlock>
          {select.map((item) => (
            <Select
              key={item.categoryTitle}
              subCategory={item.subCategory}
              categoryTitle={item.categoryTitle}
            />
          ))}
        </Styles.SelectBlock>
      </Styles.CategoryFilter>

      <Styles.Filter>
        <Styles.Title>{getTranslatedText('filterPage.filter')}</Styles.Title>

        <Styles.CheckBoxBlock>
          {checkBox.map((item) => (
            <CheckBoxes
              key={item.categoryTitle}
              subCategory={item.subCategory}
              categoryTitle={item.categoryTitle}
            />
          ))}
        </Styles.CheckBoxBlock>
      </Styles.Filter>
    </>
  );
};

export { Filtration };
