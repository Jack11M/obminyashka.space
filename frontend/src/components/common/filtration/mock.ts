// import { en } from "src/components/local";
import { getTranslatedText } from "src/components/local";

// import { lang } from ".";

export const regions = [
  { id: "90", name: "Kyivska" },
  { id: "91", name: "Dneprovska" },
  { id: "92", name: "Kharkivska" },
  { id: "93", name: "Odesska" },
  { id: "94", name: "Lvivska" },
  { id: "95", name: "Poltavska" },
  { id: "96", name: "Zaporizska" },
];

export const cities = [
  { id: "97", name: "Kyiv" },
  { id: "98", name: "Dnepr" },
  { id: "99", name: "Kharkiv" },
  { id: "100", name: "Odessa" },
  { id: "101", name: "Lviv" },
  { id: "102", name: "Poltava" },
  { id: "103", name: "Zaporizia" },
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
      id: id.toString(),
      name: "category",
      title: getTranslatedText(`categories.${name}`),
      hiddenCheckbox: true,
      subCategories: subcategories.map(({ id, name }) => {
        return {
          id: id.toString(),
          name: getTranslatedText(`categories.${name}`),
        };
      }),
    };
  });
};

export const generateFilterData = (
  ages: string[],
  sex: string[],
  seasons: string[],
  sizeShoes: string[],
  sizeClothes: string[],
  lang: string
) => {
  return [
    {
      id: "9",
      name: "location",
      title: "location",
      type: "input",
      subCategories: [
        {
          id: "52",
          name: "region",
          containerName: "city",
        },
        {
          id: "53",
          name: "city",
        },
      ],
    },
    {
      name: "sex",
      title: "sex",
      type: "radio",
      subCategories: sex.map((gender: string) => ({
        name: getTranslatedText(`genderEnum.${gender}`),
        paramToSet: gender,
      })),
    },
    {
      name: "age",
      title: "age",
      type: "checkbox",
      subCategories: ages.map((age: string) => ({
        name: age,
      })),
    },
    {
      name: "size(clothes)",
      title: "size(clothes)",
      type: "checkbox",
      subCategories: sizeClothes.map((size) => ({
        name: size + (lang === "en" ? " cm" : " см"),
        paramToSet: size,
      })),
    },
    {
      name: "size(shoes)",
      title: "size(shoes)",
      type: "checkbox",
      subCategories: sizeShoes.map((size) => ({
        name: size + (lang === "en" ? " cm" : " см"),
        paramToSet: size,
      })),
    },
    {
      name: "season",
      title: "season",
      type: "checkbox",
      subCategories: seasons.map((season: string) => ({
        name: getTranslatedText(`seasonEnum.${season}`),
        paramToSet: season,
      })),
    },
  ];
};
