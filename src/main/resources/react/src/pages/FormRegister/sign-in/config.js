import * as Yup from 'yup';

import { NO_SPACE } from 'config';
import { getTranslatedText } from 'components/local';

export const validationSchema = Yup.object().shape({
  usernameOrEmail: Yup.string()
    .required(getTranslatedText('errors.requireField'))
    .matches(NO_SPACE, getTranslatedText('errors.noSpace'))
    .default(() => ''),
  password: Yup.string()
    .required(getTranslatedText('errors.requireField'))
    .matches(NO_SPACE, getTranslatedText('errors.noSpace'))
    .default(() => ''),
  isLocalStorage: Yup.boolean().default(false),
});
