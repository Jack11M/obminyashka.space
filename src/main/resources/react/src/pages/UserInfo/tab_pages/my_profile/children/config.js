import * as Yup from 'yup';
import { enumSex } from 'config/ENUM';

export const getInitialValues = (props) => {
  return {
    children: props?.children || [
      {
        birthDate: new Date(),
        id: 0,
        sex: enumSex.UNSELECTED,
      },
    ],
  };
};

export const validationSchema = Yup.object().shape({
  children: Yup.array().of(
    Yup.object().shape({
      birthDate: Yup.string().required(),
      sex: Yup.string(),
    })
  ),
});
