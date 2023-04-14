import * as Yup from 'yup';

import { getTranslatedText } from 'components/local';

export const getValidationAdv = ({
  age,
  size,
  gender,
  season,
  readyOffer,
  locationId,
  imageFiles,
  description,
  exchangeList,
  categoryItems,
  subCategoryItems,
  announcementTitle,
}) =>
  Yup.object().shape({
    id: Yup.number().default(() => 0),
    dealType: Yup.string().default(() => 'EXCHANGE'),
    categoryId: Yup.number()
      .required(getTranslatedText('errors.requireField'))
      .default(() => categoryItems?.id),
    subcategoryId: Yup.number()
      .required(getTranslatedText('errors.requireField'))
      .default(() => subCategoryItems?.id),
    topic: Yup.string()
      .required(getTranslatedText('errors.requireField'))
      .min(3, getTranslatedText('errors.min3'))
      .max(40, getTranslatedText('errors.max40'))
      .default(() => announcementTitle),
    readyForOffers: Yup.boolean().default(() => !!readyOffer.length),
    wishesToExchange: Yup.string()
      .required(getTranslatedText('errors.requireField'))
      .max(200, getTranslatedText('errors.max200'))
      .default(() => exchangeList.join(',')),
    age: Yup.string()
      .required(getTranslatedText('errors.requireField'))
      .default(() => age.join('')),
    gender: Yup.string()
      .required(getTranslatedText('errors.requireField'))
      .default(() => gender.join('')),
    season: Yup.string()
      .required()
      .default(() => season.join('')),
    size: Yup.string()
      .when('categoryId', {
        is: (value) => value === 1,
        then: Yup.string().required(getTranslatedText('errors.requireField')),
      })
      .when('categoryId', {
        is: (value) => value === 2,
        then: Yup.string().required(getTranslatedText('errors.requireField')),
      })
      .default(() => size),
    description: Yup.string().default(() => description),
    locationId: Yup.string()
      .test('', getTranslatedText('errors.requireField'), (id) => !!id?.length)
      .default(() => locationId),
    images: Yup.array()
      .test('', getTranslatedText('errors.minPhoto'), (photo) => !!photo.length)
      .default(() => imageFiles),
  });
