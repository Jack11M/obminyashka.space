import * as yup from 'yup';

import { NO_SPACE, PHONE_REG_EXP } from 'src/config';
import { getTranslatedText } from 'src/components/local/localization';

const errorMessage = (validatedObject, message) =>
  validatedObject.value.length > 0 ? message : undefined;

export const validationUserSchema = yup.object().shape({
  firstName: yup
    .string()
    .min(2, (obj) => errorMessage(obj, getTranslatedText('errors.min2')))
    .max(50, (obj) => errorMessage(obj, getTranslatedText('errors.max50')))
    .matches(NO_SPACE, (obj) => errorMessage(obj, getTranslatedText('errors.noSpace'))),

  lastName: yup
    .string()
    .min(2, (obj) => errorMessage(obj, getTranslatedText('errors.min2')))
    .max(50, (obj) => errorMessage(obj, getTranslatedText('errors.max50')))
    .matches(NO_SPACE, (obj) => errorMessage(obj, getTranslatedText('errors.noSpace'))),

  phones: yup
    .array()
    .of(yup.string().matches(PHONE_REG_EXP, getTranslatedText('errors.phoneMatch'))),
});

export const getInitialUser = ({ lastName, firstName, phoneForInitial }) => ({
  lastName: lastName || '',
  firstName: firstName || '',
  phones: phoneForInitial,
});
