import { getTranslatedText } from "src/components/local";

export const regions = [
  { value: "90", text: "Kyivska" },
  { value: "91", text: "Dneprovska" },
  { value: "92", text: "Kharkivska" },
  { value: "93", text: "Odesska" },
  { value: "94", text: "Lvivska" },
  { value: "95", text: "Poltavska" },
  { value: "96", text: "Zaporizska" },
];

export const cities = [
  { value: "97", text: "Kyiv" },
  { value: "98", text: "Dnepr" },
  { value: "99", text: "Kharkiv" },
  { value: "100", text: "Odessa" },
  { value: "101", text: "Lviv" },
  { value: "102", text: "Poltava" },
  { value: "103", text: "Zaporizia" },
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
