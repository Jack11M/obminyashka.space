/* eslint-disable */
// @ts-nocheck
import { useSelector } from "react-redux";
import { useSearchParams } from "react-router-dom";
import { useContext, useEffect, useState } from "react";
import { showMessage, ButtonNew, Select } from "obminyashka-components";

import api from "src/REST/Resources";
import { enumAge } from "src/config/ENUM";
import { getAuthLang } from "src/store/auth/slice";
import { en, ua, getTranslatedText } from "src/components/local";

import { SearchContext } from "..";
import { ICategories, IOnChangeValue, ISelect } from "./types";
import {
  cities,
  regions,
  generateFilterData,
  generateCategoriesData,
} from "./mock";

import * as Styles from "./styles";

const Filtration = ({ submit, isLoading }: ISelect) => {
  const lang = useSelector(getAuthLang);

  const { search } = useContext(SearchContext);

  const [open, setOpen] = useState<number>(-1);
  const [isOpenLocation, setIsOpenLocation] = useState<boolean>(false);

  const [searchParams, setSearchParams] = useSearchParams();
  const [params, setParams] = useState<{
    [key: string]: string[] | number[] | string | number;
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

  const currentParams = Object.fromEntries(searchParams);

  const setOpenCategory = (id: number) => {
    if (open === id) return;
    setOpen(id);
  };

  const onChange = (values: IOnChangeValue, isCategory?: boolean) => {
    const subCategories = values.chosenOptions.map(({ value }) => value);

    if (isCategory) {
      currentParams.categoryId = values.value;

      if (subCategories.length > 0)
        currentParams.subcategoriesIdValues = JSON.stringify(
          subCategories.map((category) => Number(category))
        );

      if (subCategories.length === 0)
        delete currentParams.subcategoriesIdValues;

      if (values.value !== "2") delete currentParams.shoesSizes;
      if (values.value !== "1") delete currentParams.clothingSizes;
    }

    if (!isCategory) {
      if (values.value === "gender" && subCategories.length > 0)
        currentParams[values.value] = JSON.stringify(subCategories[0]);

      if (values.value !== "gender" && subCategories.length > 0)
        currentParams[values.value] = JSON.stringify(subCategories);

      if (subCategories.length === 0) delete currentParams[values.value];
    }

    setSearchParams(currentParams);
  };

  useEffect(() => {
    if (!search.length) {
      delete currentParams.search;
      setSearchParams(currentParams);
    }
  }, [search]);

  useEffect(() => {
    const openCategory = searchParams.get("categoryId");
    const searchResults = search || searchParams.get("search");

    if (openCategory) setOpenCategory(+openCategory - 1);

    if (searchResults) {
      const newParams = { ...currentParams, search: searchResults };
      setSearchParams(newParams);
    }

    const paramsForFiltering: {
      [key: string]: string[] | number[] | string | number;
    } = {};

    searchParams.forEach(
      (value, key) =>
        (paramsForFiltering[key] = key === "search" ? value : JSON.parse(value))
    );

    setParams(paramsForFiltering);

    setTimeout(() => {
      setParams({});
    }, 1000);
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
    <Styles.FiltrationWrapper>
      <Styles.CategoryWrapper>
        <Styles.CategoryTitle>
          {getTranslatedText("categoriesTitle.category")}
        </Styles.CategoryTitle>
        <Styles.CategoryUnderline />

        {receivedCategories &&
          generateCategoriesData(receivedCategories).map((category, index) => {
            const filteredParameterOptions: { value: string; text: any }[] = [];

            const subCategories: number[] =
              params.subcategoriesIdValues as number[];

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
                key={"category" + index}
                isActive={open === index}
                options={category.options}
                multiple={category.multiple}
                notCheckbox={category.notCheckbox}
                deleteOnClose={category.deleteOnClose}
                setIsActive={() => setOpenCategory(index)}
                filteredParameterOptions={filteredParameterOptions}
                onChange={(values: IOnChangeValue) => onChange(values, true)}
              />
            );
          })}
      </Styles.CategoryWrapper>

      <Styles.CategoryWrapper>
        <Styles.CategoryTitle>
          {getTranslatedText("categoriesTitle.filter")}
        </Styles.CategoryTitle>
        <Styles.CategoryUnderline />

        <Styles.LocationTitleContainer>
          <Styles.LocationTitle
            readOnly
            value={getTranslatedText("filter.location")}
            onClick={() => setIsOpenLocation(!isOpenLocation)}
          />

          <Styles.Triangle isOpen={isOpenLocation} />
        </Styles.LocationTitleContainer>

        <Styles.SubCategories filtration isOpen={isOpenLocation}>
          <Styles.SubCategory filtration>
            <Select
              // isLoading
              filtration
              value="region"
              title="region"
              options={regions}
              onChange={(values: any) => console.log(values)}
            />
          </Styles.SubCategory>

          <Styles.SubCategory
            filtration
            style={{
              top: "55px",
              zIndex: "99",
            }}
          >
            <Select
              // isLoading
              filtration
              value="city"
              title="city"
              options={cities}
              onChange={(values: any) => console.log(values)}
            />
          </Styles.SubCategory>
        </Styles.SubCategories>

        {generateFilterData(
          lang,
          sexShow,
          agesShow,
          seasonShow,
          receivedShoeSizes,
          receivedClothingSizes
        ).map((category, index) => {
          let filteredParameterOptions: { value: string; text: any }[] = [];

          const paramsValues: string[] = Array.isArray(params[category.value])
            ? (params[category.value] as string[])
            : [params[category.value] as string];

          category.options.filter((option) => {
            if (paramsValues.includes(option.value)) {
              filteredParameterOptions.push(option);
            }
          });

          return (
            <Select
              {...category}
              key={"filter" + index}
              multiple={category.multiple}
              filteredParameterOptions={filteredParameterOptions}
              onChange={(values: IOnChangeValue) => onChange(values)}
              disabled={
                category.disabled === undefined
                  ? undefined
                  : !(open === category.disabled)
              }
            />
          );
        })}
      </Styles.CategoryWrapper>

      <ButtonNew
        animated
        text="submit"
        height="50px"
        onClick={submit}
        colorType={"blue"}
        disabled={isLoading}
        styleType={"default"}
      />
    </Styles.FiltrationWrapper>
  );
};

export { Filtration };
