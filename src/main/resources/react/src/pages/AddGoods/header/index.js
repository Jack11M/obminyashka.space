import React, { useEffect } from 'react';
import { useSelect } from 'react-select-search';

import { clothes } from 'assets/img/all_images_export/navItems';

import {
  Image,
  Select,
  TitleH3,
  Sections,
  AddChoose,
  InputText,
  SelectTitle,
  SectionsItem,
  ItemDescription,
} from './styles';
import { getCategoryAll } from 'REST/Resources';

const DropDownSelect = () => {
  const [snapshot, valueProps, optionProps] = useSelect({
    // options,
    // value,
    // multiple,
    // disabled,
  });

  useEffect(() => {
    (async () => {
      const categories = await getCategoryAll();
      console.log(categories);
    })();
  }, []);

  return (
    <AddChoose>
      <TitleH3 className="add-title">Выберите раздел</TitleH3>
      <Sections>
        <SectionsItem className="sections_item">
          <ItemDescription>
            <span className="span_star">*</span> Категория
          </ItemDescription>
          <Select>
            <Image src={clothes} alt="clothes" />
            <SelectTitle>Одежда</SelectTitle>
          </Select>
        </SectionsItem>
        <SectionsItem className="sections_item">
          <ItemDescription>
            <span className="span_star">*</span> Подкатегория
          </ItemDescription>
          <Select>
            <SelectTitle>Колготки, носки</SelectTitle>
          </Select>
        </SectionsItem>
        <SectionsItem className="sections_item">
          <ItemDescription>
            <span className="span_star">*</span> Заголовок обьявления
          </ItemDescription>
          <InputText type="text" />
        </SectionsItem>
      </Sections>
    </AddChoose>
  );
};

export { DropDownSelect };
