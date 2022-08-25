import * as yup from 'yup';

import { getTranslatedText } from 'components/local/localization';
import { NO_SPACE, PASSWORD_REG_EXP, PASSWORD_ALT_CODE_EXP } from 'config';

export const password = yup
  .string()
  .required(getTranslatedText('errors.requireField'))
  .min(8, getTranslatedText('errors.min8'))
  .max(30, getTranslatedText('errors.max30'))
  .matches(PASSWORD_REG_EXP, getTranslatedText('errors.passwordMatch'))
  .matches(PASSWORD_ALT_CODE_EXP, getTranslatedText('errors.passwordMatch'))
  .matches(NO_SPACE, getTranslatedText('errors.noSpace'));

export const confirmPassword = yup
  .string()
  .oneOf([yup.ref('password')], getTranslatedText('errors.passwordMismatch'))
  .required(getTranslatedText('errors.requireField'));
