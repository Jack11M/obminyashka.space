import { getTranslatedText } from "src/components/local";

export const regions = [
  { value: "90", name: "Kyivska" },
  { value: "91", name: "Dneprovska" },
  { value: "92", name: "Kharkivska" },
  { value: "93", name: "Odesska" },
  { value: "94", name: "Lvivska" },
  { value: "95", name: "Poltavska" },
  { value: "96", name: "Zaporizska" },
];

export const cities = [
  { value: "97", name: "Kyiv" },
  { value: "98", name: "Dnepr" },
  { value: "99", name: "Kharkiv" },
  { value: "100", name: "Odessa" },
  { value: "101", name: "Lviv" },
  { value: "102", name: "Poltava" },
  { value: "103", name: "Zaporizia" },
];

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
      paramToSetTitle: name.toLowerCase(),
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
      title: "sex",
      paramToSetTitle: "sex",
      options: sex.map((gender: string) => ({
        text: getTranslatedText(`genderEnum.${gender}`),
      })),
    },
    {
      title: "age",
      multiple: true,
      paramToSetTitle: "age",
      options: ages.map((age: string) => ({
        text: age,
      })),
    },
    {
      title: "size(clothes)",
      disabled: 0,
      multiple: true,
      paramToSetTitle: "size(clothes)",
      options: sizeClothes.map((size) => ({
        text: size + (lang === "en" ? " cm" : " см"),
      })),
    },
    {
      title: "size(shoes)",
      disabled: 1,
      multiple: true,

      paramToSetTitle: "size(shoes)",
      options: sizeShoes.map((size) => ({
        text: size + (lang === "en" ? " cm" : " см"),
      })),
    },
    {
      title: "season",
      multiple: true,
      paramToSetTitle: "season",
      options: seasons.map((season: string) => ({
        text: getTranslatedText(`seasonEnum.${season}`),
      })),
    },
  ];
};
