import * as yup from 'yup';

import { password, confirmPassword } from 'Utils/validation';
import { getTranslatedText } from 'components/local/localization';
import { EMAIL_REG_EXP, PASSWORD_ALT_CODE_EXP, PASSWORD_REG_EXP } from 'config';

export const validationPasswordSchema = yup.object().shape({
  oldPassword: yup
    .string()
    .min(8, getTranslatedText('errors.min8'))
    .max(30, getTranslatedText('errors.max30'))
    .matches(PASSWORD_REG_EXP, getTranslatedText('errors.passwordMatch'))
    .matches(PASSWORD_ALT_CODE_EXP, getTranslatedText('errors.altCodeMatch'))
    .required(getTranslatedText('errors.requireField'))
    .default(() => ''),
  newPassword: yup
    .string()
    .min(8, getTranslatedText('errors.min8'))
    .max(30, getTranslatedText('errors.max30'))
    .matches(PASSWORD_REG_EXP, getTranslatedText('errors.passwordMatch'))
    .matches(PASSWORD_ALT_CODE_EXP, getTranslatedText('errors.altCodeMatch'))
    .notOneOf(
      [yup.ref('oldPassword'), false],
      getTranslatedText('errors.passwordIdentical')
    )
    .required(getTranslatedText('errors.requireField'))
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

export const validationEmailSchema = (currentEmail) =>
  yup.object().shape({
    newEmail: yup
      .string()
      .notOneOf([currentEmail], getTranslatedText('errors.emailIdentical'))
      .matches(EMAIL_REG_EXP, getTranslatedText('errors.max129'))
      .email(getTranslatedText('errors.invalidEmailFormat'))
      .required(getTranslatedText('errors.requireField'))
      .default(() => ''),
    newEmailConfirmation: yup
      .string()
      .oneOf(
        [yup.ref('newEmail')],
        getTranslatedText('errors.emailNotIdentical')
      )
      .email(getTranslatedText('errors.invalidEmailFormat'))
      .required(getTranslatedText('errors.requireField'))
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
