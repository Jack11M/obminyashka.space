import { getTranslatedText } from "src/components/local";

export const cities = [
  { value: "97", text: "Kyiv" },
  { value: "98", text: "Dnepr" },
  { value: "99", text: "Kharkiv" },
  { value: "100", text: "Odessa" },
  { value: "101", text: "Lviv" },
  { value: "102", text: "Poltava" },
  { value: "103", text: "Zaporizia" },
];

interface Options {
  value: string;
  text: string | number;
}

interface Category {
  value: string;
  options: Options[];
}

export const filteredParameterOptions = (
  category: Category,
  params: {
    [key: string]: string[] | number[] | string | number;
  },
  isFilter?: boolean
) => {
  const filteredParameterOptions: Options[] = [];

  if (isFilter) {
    const paramsValues: string[] = Array.isArray(params[category.value])
      ? (params[category.value] as string[])
      : [params[category.value] as string];

    category.options.filter((option) => {
      if (paramsValues.includes(option.value)) {
        filteredParameterOptions.push(option);
      }
    });
  }

  if (!isFilter) {
    const subCategories: number[] = params.subcategoriesIdValues as number[];

    if (subCategories) {
      category.options.filter((option) => {
        if (subCategories.includes(+option.value)) {
          filteredParameterOptions.push(option);
        }
      });
    }
  }

  return filteredParameterOptions;
};

export const generateArea = (
  lang: string,
  data: {
    id: string;
    nameEn: string;
    nameUa: string;
  }[]
) =>
  data.map((el) => {
    return {
      value: el.id,
      text: lang === "en" ? el.nameEn : el.nameUa,
    };
  });

export const generateCategoriesData = (
  data: {
    id: number;
    name: string;
    subcategories: { id: number; name: string }[];
  }[]
) => {
  return data.map(({ id, name, subcategories }) => {
    return {
      value: id.toString(),
      title: getTranslatedText(`categories.${name}`),
      multiple: true,
      notCheckbox: true,
      deleteOnClose: true,
      options: subcategories.map(({ id, name }) => {
        return {
          value: id.toString(),
          text: getTranslatedText(`categories.${name}`),
        };
      }),
    };
  });
};

export const generateFilterData = (
  lang: string,
  sex: string[],
  ages: string[],
  seasons: string[],
  sizeShoes: string[],
  sizeClothes: string[]
) => {
  return [
    {
      value: "gender",
      title: getTranslatedText("filter.gender"),
      options: sex.map((gender: string) => ({
        value: gender,
        text: getTranslatedText(`genderEnum.${gender}`),
      })),
    },
    {
      value: "age",
      title: getTranslatedText("filter.age"),
      multiple: true,
      options: ages.map((age: string) => ({
        value: age.replace(/ /g, ""),
        text: age,
      })),
    },
    {
      value: "clothingSizes",
      title: getTranslatedText("filter.clothingSizes"),
      disabled: 0,
      multiple: true,
      options: sizeClothes.map((size) => ({
        value: size,
        text: size + (lang === "en" ? " cm" : " см"),
      })),
    },
    {
      value: "shoesSizes",
      title: getTranslatedText("filter.shoesSizes"),
      disabled: 1,
      multiple: true,
      options: sizeShoes.map((size) => ({
        value: size,
        text: size + (lang === "en" ? " cm" : " см"),
      })),
    },
    {
      value: "season",
      title: getTranslatedText("filter.season"),
      multiple: true,
      options: seasons.map((season: string) => ({
        value: season,
        text: getTranslatedText(`seasonEnum.${season}`),
      })),
    },
  ];
};
