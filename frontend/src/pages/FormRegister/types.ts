//TODO Change types any
import { FormikHelpers } from "formik";

export type FormikBagType = FormikHelpers<any>;

export interface IButtonsWrapper {
  register?: boolean;
  isLoading?: boolean;
}
