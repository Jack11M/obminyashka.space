/* eslint-disable */
// @ts-nocheck
// TODO: fix typescript
import { useSelector } from "react-redux";
import { useEffect, useState } from "react";
import { useSearchParams } from "react-router-dom";
import { showMessage, Select, ButtonNew } from "obminyashka-components";

import api from "src/REST/Resources";
import { enumAge } from "src/config/ENUM";
import { en, ua } from "src/components/local";
import { getAuthLang } from "src/store/auth/slice";
import {
  cities,
  regions,
  generateFilterData,
  generateCategoriesData,
} from "./mock";

import * as Styles from "./styles";

export interface ISelectOption {
  text: string;
  value?: string;
  [key: string]: any;
}

export interface IOnChangeValue {
  value: string;
  paramToSetTitle: string;
  chosenOptions: ISelectOption[] | [];
}

const Filtration = () => {
  const lang = useSelector(getAuthLang);

  const [open, setOpen] = useState<number>(-1);
  const [isOpenLocation, setIsOpenLocation] = useState<boolean>(false);

  const [searchParams, setSearchParams] = useSearchParams();
  const [receivedShoeSizes, setReceivedShoeSize] = useState([]);
  const [receivedCategories, setReceivedCategories] = useState([]);
  const [receivedClothingSizes, setReceivedClothingSize] = useState([]);

  const { seasonEnum, genderEnum } = lang === "en" ? en : ua;

  const agesShow = Object.values(enumAge);
  const sexShow = Object.keys(genderEnum);
  const seasonShow = Object.keys(seasonEnum);

  const setOpenCategory = (id: number) => {
    if (open === id) {
      return;
    }

    setOpen(id);
  };

  const onChange = (values: IOnChangeValue) => {
    const searchParams = new URLSearchParams(window.location.search);
    const paramsMap: { [key: string]: string[] } = {};

    // Заполняем объект параметров из текущей строки запроса
    searchParams.forEach((value, key) => {
      if (!paramsMap[key]) {
        paramsMap[key] = [];
      }

      if (value.includes(",")) {
        paramsMap[key] = value.split(","); // Если значение содержит запятую, разделяем его на массив строк
      } else {
        paramsMap[key].push(value);
      }
    });

    // Обновляем или добавляем значения для параметра
    const paramName = values.paramToSetTitle;
    const chosenOptions = values.chosenOptions.map((el) =>
      el.value ? el.value : el.text.replace(/'/g, "").trim()
    );

    if (chosenOptions.length === 0) {
      delete paramsMap[paramName]; // Удаляем параметр, если нет выбранных опций
    } else {
      if (!paramsMap[paramName]) {
        paramsMap[paramName] = [];
      }
      chosenOptions.forEach((option) => {
        if (!paramsMap[paramName].includes(option)) {
          paramsMap[paramName].push(option);
        }
      });
    }

    // Преобразуем объект параметров в строку запроса с сохранением порядка
    let queryString = "";
    Object.keys(paramsMap).forEach((key, index) => {
      if (paramsMap[key].length > 0) {
        if (queryString.length > 0) {
          queryString += "&";
        }
        queryString += `${key}=${paramsMap[key].join(",")}`;
      }
    });

    // Обновляем URL с новой строкой запроса
    window.history.replaceState(
      {},
      "",
      `${window.location.pathname}?${queryString}`
    );

    console.log(paramsMap);
  };

  // const onChange = (values: IOnChangeValue) => {
  //   const paramName = values.paramToSetTitle;
  //   const chosenOptions = values.chosenOptions.map((el) =>
  //     el.value ? el.value : el.text.replace(/'/g, "").trim()
  //   );

  //   // Получаем текущую строку запроса
  //   const searchParams = new URLSearchParams(window.location.search);

  //   // Создаем объект для хранения параметров в порядке их добавления
  //   const paramsMap: { [key: string]: string[] } = {};

  //   // Заполняем объект параметров из текущей строки запроса
  //   searchParams.forEach((value, key) => {
  //     if (!paramsMap[key]) {
  //       paramsMap[key] = [];
  //     }
  //     paramsMap[key].push(value);
  //   });

  //   // Если chosenOptions пуст, удаляем параметр из объекта
  //   if (chosenOptions.length === 0) {
  //     delete paramsMap[paramName];
  //   } else {
  //     // Обновляем или добавляем новые значения параметра
  //     paramsMap[paramName] = chosenOptions;
  //   }

  //   // Преобразуем объект параметров в строку запроса с сохранением порядка
  //   let queryString = "";
  //   Object.keys(paramsMap).forEach((key, index) => {
  //     if (paramsMap[key].length > 0) {
  //       if (index !== 0) {
  //         queryString += "&";
  //       }
  //       queryString += `${key}=${paramsMap[key].join(",")}`;
  //     }
  //   });

  //   // Обновляем URL с новой строкой запроса
  //   window.history.replaceState(
  //     {},
  //     "",
  //     `${window.location.pathname}?${queryString}`
  //   );

  //   console.log(paramsMap);
  // };

  useEffect(() => {
    (async () => {
      try {
        const [categories, clothingSizes, shoeSizes] = await Promise.all([
          api.addGood.getCategoryAll(),
          api.addGood.getSize(1),
          api.addGood.getSize(2),
        ]);

        if (
          Array.isArray(categories) &&
          Array.isArray(clothingSizes) &&
          Array.isArray(shoeSizes)
        ) {
          setReceivedClothingSize(clothingSizes);
          setReceivedShoeSize(shoeSizes);
          setReceivedCategories(categories);
        }
      } catch (err: any) {
        showMessage.error(err.response?.data ?? err?.message);
      }
    })();
  }, []);

  // console.log(receivedCategories);

  return (
    <div>
      <div style={{ marginBottom: "20px" }}>
        <h1>Categories</h1>

        {receivedCategories &&
          generateCategoriesData(receivedCategories).map((category, index) => {
            return (
              <Select
                {...category}
                disabled={false}
                key={"category" + index}
                isActive={open === index}
                options={category.options}
                multiple={category.multiple}
                notCheckbox={category.notCheckbox}
                deleteOnClose={category.deleteOnClose}
                onChange={(values) => onChange(values)}
                setIsActive={() => setOpenCategory(index)}
              />
            );
          })}
      </div>

      <div style={{ marginBottom: "20px" }}>
        <h1>Filter</h1>

        <Styles.TitleContainer>
          <Styles.Title
            readOnly
            value={"location"}
            onClick={() => setIsOpenLocation(!isOpenLocation)}
          />

          <Styles.Triangle isOpen={isOpenLocation} />
        </Styles.TitleContainer>

        <Styles.ScrollWrapper filtration>
          <Styles.SubCategories filtration isOpen={isOpenLocation}>
            <Styles.SubCategory filtration>
              <Select
                value="99"
                filtration
                title="region"
                options={regions}
                onChange={(values) => console.log(values)}
              />
            </Styles.SubCategory>

            <Styles.SubCategory filtration>
              <Select
                value="100"
                filtration
                title="city"
                options={cities}
                onChange={(values) => console.log(values)}
              />
            </Styles.SubCategory>
          </Styles.SubCategories>
        </Styles.ScrollWrapper>

        {generateFilterData(
          lang,
          sexShow,
          agesShow,
          seasonShow,
          receivedShoeSizes,
          receivedClothingSizes
        ).map((category, index) => {
          return (
            <Select
              {...category}
              key={"filter" + index}
              multiple={category.multiple}
              onChange={(values) => onChange(values)}
              disabled={
                category.disabled === undefined
                  ? undefined
                  : !(open === category.disabled)
              }
            />
          );
        })}
      </div>

      <div style={{ margin: "10px 0", width: "334px" }}>
        <ButtonNew colorType={"blue"} styleType={"default"} text="submit" />
      </div>
    </div>
  );
};

export { Filtration };
