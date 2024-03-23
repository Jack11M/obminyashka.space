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
      title: "sex",
      options: sex.map((gender: string) => ({
        value: gender,
        text: getTranslatedText(`genderEnum.${gender}`),
      })),
    },
    {
      value: "age",
      title: "age",
      multiple: true,
      options: ages.map((age: string) => ({
        value: age,
        text: age,
      })),
    },
    {
      value: "clothingSizes",
      title: "size(clothes)",
      disabled: 0,
      multiple: true,
      options: sizeClothes.map((size) => ({
        value: size,
        text: size + (lang === "en" ? " cm" : " см"),
      })),
    },
    {
      value: "shoesSizes",
      title: "size(shoes)",
      disabled: 1,
      multiple: true,
      options: sizeShoes.map((size) => ({
        value: size,
        text: size + (lang === "en" ? " cm" : " см"),
      })),
    },
    {
      value: "season",
      title: "season",
      multiple: true,
      options: seasons.map((season: string) => ({
        value: season,
        text: getTranslatedText(`seasonEnum.${season}`),
      })),
    },
  ];
};
