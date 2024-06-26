import * as Yup from "yup";

import { NO_SPACE } from "src/config";
import { getTranslatedText } from "src/components/local";

export const validationSchema = Yup.object().shape({
  usernameOrEmail: Yup.string()
    .required(getTranslatedText("errors.requireField"))
    .matches(NO_SPACE, getTranslatedText("errors.noSpace"))
    .test(
      "no-cyrillic",
      getTranslatedText("errors.invalidEmailFormat"),
      (value) => {
        return !/[а-яА-ЯёЁ]/.test(value || "");
      }
    )
    .default(() => ""),
  password: Yup.string()
    .required(getTranslatedText("errors.requireField"))
    .matches(NO_SPACE, getTranslatedText("errors.noSpace"))
    .default(() => ""),
  isLocalStorage: Yup.boolean().default(false),
});
