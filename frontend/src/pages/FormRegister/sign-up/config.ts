import * as Yup from "yup";

import { getTranslatedText } from "src/components/local";
import { confirmPassword, password } from "src/Utils/validation";
import {
  NO_SPACE,
  EMAIL_REG_EXP,
  NUMBER_OF_CHARACTERS,
  USERNAME_ALT_CODE_EXP,
} from "src/config";

export const validationSchema = Yup.object().shape({
  email: Yup.string()
    .required(getTranslatedText("errors.requireField"))
    .matches(EMAIL_REG_EXP, getTranslatedText("errors.invalidEmailFormat"))
    .matches(NUMBER_OF_CHARACTERS, getTranslatedText("errors.max129"))
    .matches(NO_SPACE, getTranslatedText("errors.noSpace"))
    .default(() => ""),
  username: Yup.string()
    .required(getTranslatedText("errors.requireField"))
    .min(2, getTranslatedText("errors.min2"))
    .max(50, getTranslatedText("errors.max50"))
    .matches(USERNAME_ALT_CODE_EXP, getTranslatedText("errors.altCodeMatch"))
    .matches(NO_SPACE, getTranslatedText("errors.noSpace"))
    .default(() => ""),
  password: password.default(() => ""),
  confirmPassword: confirmPassword.default(() => ""),
  agreement: Yup.boolean()
    .oneOf([true])
    .required()
    .default(() => false),
});
