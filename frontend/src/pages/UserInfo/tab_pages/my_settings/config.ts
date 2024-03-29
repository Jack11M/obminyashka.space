import * as yup from 'yup';

import { password, confirmPassword } from 'src/Utils/validation';
import { getTranslatedText } from 'src/components/local/localization';
import {
  NO_SPACE,
  EMAIL_REG_EXP,
  PASSWORD_REG_EXP,
  NUMBER_OF_CHARACTERS,
  PASSWORD_ALT_CODE_EXP,
} from 'src/config';

export const validationPasswordSchema = yup.object().shape({
  password: yup
    .string()
    .required(getTranslatedText('errors.requireField'))
    .min(8, getTranslatedText('errors.min8'))
    .max(30, getTranslatedText('errors.max30'))
    .matches(PASSWORD_REG_EXP, getTranslatedText('errors.passwordMatch'))
    .matches(PASSWORD_ALT_CODE_EXP, getTranslatedText('errors.altCodeMatch'))
    .matches(NO_SPACE, getTranslatedText('errors.noSpace'))
    .notOneOf([yup.ref('oldPassword')], getTranslatedText('errors.passwordIdentical'))
    .default(() => ''),
  confirmPassword: yup
    .string()
    .oneOf([yup.ref('password')], getTranslatedText('errors.passwordMismatch'))
    .required(getTranslatedText('errors.requireField'))
    .default(() => ''),
});

export const validationEmailSchema = (email: string) =>
  yup.object().shape({
    email: yup
      .string()
      .required(getTranslatedText('errors.requireField'))
      .matches(NO_SPACE, getTranslatedText('errors.noSpace'))
      .notOneOf([email], getTranslatedText('errors.emailIdentical'))
      .matches(EMAIL_REG_EXP, getTranslatedText('errors.invalidEmailFormat'))
      .matches(NUMBER_OF_CHARACTERS, getTranslatedText('errors.max129'))
      .default(() => ''),
  });

export const validationDeleteSchema = yup.object().shape({
  password,
  confirmPassword,
});

export const initialValuesDelete = {
  password: '',
  confirmPassword: '',
};

export const getInitialValueEmail = (value: string) => ({ email: value || '' });
