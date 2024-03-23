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
import { sub } from "date-fns";

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

  const onChange = (values: IOnChangeValue, isCategory?: boolean) => {
    const paramsObject = Object.fromEntries(searchParams);
    const subCategories = values.chosenOptions.map(({ value }) => value);

    if (isCategory) {
      paramsObject.category = values.value;

      if (subCategories.length > 0) {
        paramsObject.subCategories = JSON.stringify(
          subCategories.map((category) => +category)
        );
      }

      if (subCategories.length === 0) {
        delete paramsObject.subCategories;
      }

      if (values.value === "2") {
        delete paramsObject.clothingSizes;
      }

      if (values.value === "1") {
        delete paramsObject.shoesSizes;
      }
    }

    if (!isCategory) {
      if (values.value === "gender") {
        paramsObject[values.value] = subCategories[0];
      }

      if (values.value !== "gender") {
        paramsObject[values.value] = JSON.stringify(subCategories);
      }

      if (subCategories.length === 0) {
        delete paramsObject[values.value];
      }
    }

    let queryString = `category=${paramsObject["category"]}`;

    if (paramsObject.hasOwnProperty("subCategories")) {
      queryString += `&subCategories=${paramsObject["subCategories"]}`;
    }

    for (let key in paramsObject) {
      if (
        paramsObject.hasOwnProperty(key) &&
        key !== "category" &&
        key !== "subCategories"
      ) {
        queryString += `&${key}=${paramsObject[key]}`;
      }
    }

    setSearchParams(queryString);
  };

  useEffect(() => {
    const paramsMap: { [key: string]: string[] } = {};

    searchParams.forEach((value, key) => {
      paramsMap[key] = JSON.parse(value);
    });

    console.log(paramsMap);
  }, [searchParams]);

  useEffect(() => {
    const openCategory = searchParams.get("category");

    setOpenCategory(+openCategory - 1);
  }, []);

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
                setIsActive={() => setOpenCategory(index)}
                onChange={(values) => onChange(values, true)}
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
              // disabled={false}
              // disabled={open === category.disabled}
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
