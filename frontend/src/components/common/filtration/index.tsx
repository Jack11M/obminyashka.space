/* eslint-disable */
import { useSelector } from "react-redux";
import { useEffect, useState } from "react";
import { useSearchParams } from "react-router-dom";
import { showMessage, ButtonNew } from "obminyashka-components";

import api from "src/REST/Resources";
import { enumAge } from "src/config/ENUM";
import { en, ua } from "src/components/local";
import { getAuthLang } from "src/store/auth/slice";

import { ICategories, IOnChangeValue } from "./types";
import {
  cities,
  regions,
  generateFilterData,
  generateCategoriesData,
} from "./mock";

import * as Styles from "./styles";

import { Select } from "./select-new";

const Filtration = () => {
  const lang = useSelector(getAuthLang);

  const [open, setOpen] = useState<number>(-1);
  const [isOpenLocation, setIsOpenLocation] = useState<boolean>(false);

  const [searchParams, setSearchParams] = useSearchParams();
  const [params, setParams] = useState<{
    [key: string]: string[] | number[] | string;
  }>({});

  const [receivedShoeSizes, setReceivedShoeSize] = useState<string[]>([]);
  const [receivedClothingSizes, setReceivedClothingSize] = useState<string[]>(
    []
  );
  const [receivedCategories, setReceivedCategories] = useState<ICategories[]>(
    []
  );

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
          subCategories.map((category) => Number(category))
        );
      }

      if (subCategories.length === 0) {
        delete paramsObject.subCategories;
      }

      if (values.value === "1") {
        delete paramsObject.shoesSizes;
      }

      if (values.value === "2") {
        delete paramsObject.clothingSizes;
      }
    }

    if (!isCategory) {
      if (values.value === "gender") {
        paramsObject[values.value] = JSON.stringify(subCategories[0]);
      }

      if (values.value !== "gender") {
        paramsObject[values.value] = JSON.stringify(subCategories);
      }

      if (subCategories.length === 0) {
        delete paramsObject[values.value];
      }
    }

    setSearchParams(paramsObject);
  };

  useEffect(() => {
    const paramsMap: { [key: string]: string[] | number[] | string } = {};

    searchParams.forEach((value, key) => {
      paramsMap[key] = JSON.parse(value);
    });

    setParams(paramsMap);
  }, [searchParams]);

  useEffect(() => {
    const openCategory = searchParams.get("category");

    if (openCategory) {
      setOpenCategory(+openCategory - 1);
    }
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
            let filteredParameterOptions: { value: string; text: any }[] = [];
            const subCategories: number[] = params.subCategories as number[];

            if (subCategories) {
              category.options.filter((option) => {
                if (subCategories.includes(+option.value)) {
                  filteredParameterOptions.push(option);
                }
              });
            }

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
                filteredParameterOptions={filteredParameterOptions}
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
          let filteredParameterOptions: { value: string; text: any }[] = [];

          const values: string[] = Array.isArray(params[category.value])
            ? (params[category.value] as string[])
            : [params[category.value] as string];

          category.options.filter((option) => {
            if (values.includes(option.value)) {
              filteredParameterOptions.push(option);
            }
          });

          return (
            <Select
              {...category}
              params={params}
              key={"filter" + index}
              multiple={category.multiple}
              onChange={(values) => onChange(values)}
              filteredParameterOptions={filteredParameterOptions}
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
