/* eslint-disable */
// @ts-nocheck
// TODO: fix typescript
import { Formik, Form, FormikValues } from "formik";
import { useMemo, useEffect, useState } from "react";
import { useSelector } from "react-redux";
import { showMessage, FilterBlock, ButtonNew } from "obminyashka-components";

import api from "src/REST/Resources";
import { initialValues } from "./config";
import { enumAge } from "src/config/ENUM";
import { en, ua } from "src/components/local";
import { getAuthLang } from "src/store/auth/slice";
import { getTranslatedText } from "src/components/local";
import {
  filterData,
  categoryData,
  generateFilterData,
  generateCategoriesData,
} from "./mock";

const Filtration = () => {
  const lang = useSelector(getAuthLang);

  const [receivedShoeSizes, setReceivedShoeSize] = useState([]);
  const [receivedCategories, setReceivedCategories] = useState([]);
  const [receivedClothingSizes, setReceivedClothingSize] = useState([]);

  const { seasonEnum, genderEnum } = lang === "en" ? en : ua;

  const agesShow = Object.values(enumAge);
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

  // const currentType = useMemo(() => {
  //   return lang === "en" ? "cm" : "см";
  // }, [lang]);

  const getCities = async (id: string) => {
    alert(`пошел запрос с id ${id}`);
    await setTimeout(() => {
      alert(`ответ по id ${id}`);
    }, 300);

    return cities;
  };

  const onSubmit = async (values: FormikValues) => {
    const params = new URLSearchParams();
    params.append("subcategoryFilterRequest.categoryId", +values.category.id);
    params.append(
      "subcategoryFilterRequest.subcategoriesIdValues",
      values.category.subcategories[0]
    );

    // console.log(values);
    const sendData = await api.filter.getFilter(params);
  };

  return (
    <Formik initialValues={initialValues} onSubmit={onSubmit}>
      <Form>
        <div style={{ margin: "10px" }}></div>

        <FilterBlock
          categoryBlock
          categoryActive="2"
          title={"Categories"}
          categoryFilterData={
            receivedCategories && generateCategoriesData(receivedCategories)
          }
        />

        <div style={{ margin: "10px" }}></div>

        <FilterBlock
          title={"Filter"}
          getCities={getCities}
          categoryFilterData={
            agesShow &&
            generateFilterData(
              agesShow,
              sexShow,
              seasonShow,
              receivedShoeSizes,
              receivedClothingSizes,
              lang
            )
          }
        />

        <div style={{ margin: "10px 0", width: "334px" }}>
          <ButtonNew colorType={"blue"} styleType={"default"} text="submit" />
        </div>
      </Form>
    </Formik>
  );
};

export { Filtration };
