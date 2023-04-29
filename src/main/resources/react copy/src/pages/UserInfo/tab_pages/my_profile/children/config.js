import * as Yup from 'yup';
import { enumSex } from 'config/ENUM';
import { getTranslatedText } from 'components/local/localization';

export const getInitialValues = (children) => {
  const response = children?.length > 0 ? children : undefined;

  return {
    children: response || [
      {
        birthDate: null,
        id: 0,
        sex: enumSex.UNSELECTED,
      },
    ],
  };
};

export const validationSchema = Yup.object().shape({
  children: Yup.array().of(
    Yup.object().shape({
      birthDate: Yup.string()
        .nullable()
        .required(getTranslatedText('ownInfo.chooseData')),
      sex: Yup.string(),
    })
  ),
});
