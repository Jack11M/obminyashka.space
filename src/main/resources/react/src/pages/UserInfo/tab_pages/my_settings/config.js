import * as yup from 'yup';

import { password, confirmPassword } from 'Utils/validation';
import { getTranslatedText } from 'components/local/localization';
import {
  NO_SPACE,
  EMAIL_REG_EXP,
  PASSWORD_REG_EXP,
  NUMBER_OF_CHARACTERS,
  PASSWORD_ALT_CODE_EXP,
} from 'config';

export const validationPasswordSchema = yup.object().shape({
  oldPassword: yup
    .string()
    .required(getTranslatedText('errors.requireField'))
    .min(8, getTranslatedText('errors.min8'))
    .max(30, getTranslatedText('errors.max30'))
    .matches(PASSWORD_REG_EXP, getTranslatedText('errors.passwordMatch'))
    .matches(PASSWORD_ALT_CODE_EXP, getTranslatedText('errors.altCodeMatch'))
    .matches(NO_SPACE, getTranslatedText('errors.noSpace'))
    .default(() => ''),
  newPassword: yup
    .string()
    .required(getTranslatedText('errors.requireField'))
    .min(8, getTranslatedText('errors.min8'))
    .max(30, getTranslatedText('errors.max30'))
    .matches(PASSWORD_REG_EXP, getTranslatedText('errors.passwordMatch'))
    .matches(PASSWORD_ALT_CODE_EXP, getTranslatedText('errors.altCodeMatch'))
    .matches(NO_SPACE, getTranslatedText('errors.noSpace'))
    .notOneOf(
      [yup.ref('oldPassword'), false],
      getTranslatedText('errors.passwordIdentical')
    )
    .default(() => ''),
  confirmNewPassword: yup
    .string()
    .oneOf(
      [yup.ref('newPassword')],
      getTranslatedText('errors.passwordMismatch')
    )
    .required(getTranslatedText('errors.requireField'))
    .default(() => ''),
});

export const validationEmailSchema = (email) =>
  yup.object().shape({
    email: yup
      .string()
      .required(getTranslatedText('errors.requireField'))
      .matches(NO_SPACE, getTranslatedText('errors.noSpace'))
      .notOneOf([email], getTranslatedText('errors.emailIdentical'))
      .matches(EMAIL_REG_EXP, getTranslatedText('errors.invalidEmailFormat'))
      .matches(NUMBER_OF_CHARACTERS, getTranslatedText('errors.max129'))
      .default(() => ''),
    /* newEmailConfirmation: yup
      .string()
      .required(getTranslatedText('errors.requireField'))
      .oneOf(
        [yup.ref('newEmail')],
        getTranslatedText('errors.emailNotIdentical')
      )
      .default(() => ''), */
  });

export const validationDeleteSchema = yup.object().shape({
  password,
  confirmPassword,
});

export const initialValuesDelete = {
  password: '',
  confirmPassword: '',
};
