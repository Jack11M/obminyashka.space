import { memo, useContext, useState } from 'react';
import * as yup from 'yup';
import { Formik } from 'formik';
import { Link } from 'react-router-dom';
import { useDispatch, useSelector } from 'react-redux';

import api from 'REST/Resources';
import { route } from 'routes/routeConstants';
import { Button } from 'components/common/buttons';
import { ModalContext } from 'components/common/pop-up';
import { putEmail, getLang, getProfile } from 'store/auth/slice';
import { getTranslatedText } from 'components/local/localization';
import TitleBigBlue from 'components/common/title_Big_Blue/title_Big_Blue';
import { EMAIL_REG_EXP, PASSWORD_ALT_CODE_EXP, PASSWORD_REG_EXP } from 'config';

import InputProfile from '../../components/inputProfile';

import './mySettings.scss';

const MySettings = () => {
  const { openModal } = useContext(ModalContext);
  const dispatch = useDispatch();

  const [isFetchPass, setIsFetchPass] = useState(false);
  const [isFetchEmail, setIsFetchEmail] = useState(false);

  const lang = useSelector(getLang);
  const { email: currentEmail } = useSelector(getProfile);

  const validationPasswordSchema = yup.object().shape({
    oldPassword: yup
      .string()
      .min(8, getTranslatedText('errors.min8', lang))
      .max(30, getTranslatedText('errors.max30', lang))
      .matches(
        PASSWORD_REG_EXP,
        getTranslatedText('errors.passwordMatch', lang)
      )
      .matches(
        PASSWORD_ALT_CODE_EXP,
        getTranslatedText('errors.altCodeMatch', lang)
      )
      .required(getTranslatedText('errors.requireField', lang))
      .default(() => ''),
    newPassword: yup
      .string()
      .min(8, getTranslatedText('errors.min8', lang))
      .max(30, getTranslatedText('errors.max30', lang))
      .matches(
        PASSWORD_REG_EXP,
        getTranslatedText('errors.passwordMatch', lang)
      )
      .matches(
        PASSWORD_ALT_CODE_EXP,
        getTranslatedText('errors.altCodeMatch', lang)
      )
      .notOneOf(
        [yup.ref('oldPassword'), false],
        getTranslatedText('errors.passwordIdentical', lang)
      )
      .required(getTranslatedText('errors.requireField', lang))
      .default(() => ''),
    confirmNewPassword: yup
      .string()
      .oneOf(
        [yup.ref('newPassword')],
        getTranslatedText('errors.passwordMismatch', lang)
      )
      .required(getTranslatedText('errors.requireField', lang))
      .default(() => ''),
  });

  const validationEmailSchema = yup.object().shape({
    newEmail: yup
      .string()
      .notOneOf(
        [currentEmail],
        getTranslatedText('errors.emailIdentical', lang)
      )
      .matches(EMAIL_REG_EXP, getTranslatedText('errors.max129', lang))
      .email(getTranslatedText('errors.invalidEmailFormat', lang))
      .required(getTranslatedText('errors.requireField', lang))
      .default(() => ''),
    newEmailConfirmation: yup
      .string()
      .oneOf(
        [yup.ref('newEmail')],
        getTranslatedText('errors.emailNotIdentical', lang)
      )
      .email(getTranslatedText('errors.invalidEmailFormat', lang))
      .required(getTranslatedText('errors.requireField', lang))
      .default(() => ''),
  });

  const initialPasswordValues = validationPasswordSchema.cast({});
  const initialEmailValues = validationEmailSchema.cast({});

  return (
    <>
      <TitleBigBlue
        // whatClass="myProfile-title"
        style={{ margin: '65px 0 40px' }}
        text={getTranslatedText('settings.changePassword', lang)}
      />
      <Formik
        initialValues={initialPasswordValues}
        validateOnBlur
        validationSchema={validationPasswordSchema}
        onSubmit={async (values, onSubmitProps) => {
          setIsFetchPass(true);
          try {
            const { data } = await api.fetchProfile.putPasswordFetch(values);
            openModal({
              title: getTranslatedText('popup.serverResponse', lang),
              children: <p>{data}</p>,
            });
            onSubmitProps.resetForm();
            setIsFetchPass(false);
          } catch (e) {
            setIsFetchPass(false);
            if (e.response.status === 400) {
              onSubmitProps.setErrors({ oldPassword: e.response.data.error });
            }
          }
        }}
      >
        {({ errors, isValid, handleSubmit, dirty }) => (
          <>
            <div className="info-one">
              <InputProfile
                type="password"
                name="oldPassword"
                label={getTranslatedText('settings.currentPassword', lang)}
              />
              <InputProfile
                type="password"
                name="newPassword"
                label={getTranslatedText('settings.newPassword', lang)}
              />
              <InputProfile
                type="password"
                name="confirmNewPassword"
                label={getTranslatedText('settings.confirmPassword', lang)}
              />
            </div>

            <Button
              type="submit"
              width="248px"
              height="49px"
              whatClass="btn-profile"
              isLoading={isFetchPass}
              disabling={!dirty && !isValid}
              text={getTranslatedText('button.save', lang)}
              click={!errors.oldPassword ? handleSubmit : null}
            />
          </>
        )}
      </Formik>

      <TitleBigBlue
        // whatClass="myProfile-title"
        style={{ margin: '65px 0 40px' }}
        text={getTranslatedText('settings.changeEmail', lang)}
      />
      <Formik
        validateOnBlur
        initialValues={initialEmailValues}
        validationSchema={validationEmailSchema}
        onSubmit={async (values, onSubmitProps) => {
          const { newEmail, newEmailConfirmation } = values;
          setIsFetchEmail(true);
          try {
            const { data } = await api.fetchProfile.putEmailFetch({
              newEmail,
              newEmailConfirmation,
            });
            openModal({
              title: getTranslatedText('popup.serverResponse', lang),
              children: <p>{data}</p>,
            });
            dispatch(putEmail(newEmail));
            onSubmitProps.resetForm();
            setIsFetchEmail(false);
          } catch (e) {
            setIsFetchEmail(false);
            if (e.response.status === 409) {
              onSubmitProps.setErrors({
                newEmailConfirmation: e.response.data.error,
              });
            }
            if (e.response.status === 400) {
              console.log(e.response.data.error);
              onSubmitProps.setErrors({ newEmail: e.response.data.error });
            }
          }
        }}
      >
        {({ errors, isValid, handleSubmit, dirty }) => (
          <>
            <div className="info-one">
              <InputProfile
                readOnly
                type="email"
                name="oldEmail"
                value={currentEmail}
                label={getTranslatedText('settings.oldEmail', lang)}
              />
              <InputProfile
                type="email"
                name="newEmail"
                label={getTranslatedText('settings.newEmail', lang)}
              />
              <InputProfile
                type="email"
                name="newEmailConfirmation"
                label={getTranslatedText('settings.confirmEmail', lang)}
              />
            </div>

            <Button
              type="submit"
              width="300px"
              height="49px"
              isLoading={isFetchEmail}
              disabling={!isValid && !dirty}
              whatClass="btn-profile e-mail-button"
              click={!errors.newEmail ? handleSubmit : null}
              text={getTranslatedText('button.saveEmail', lang)}
            />
          </>
        )}
      </Formik>
      <TitleBigBlue
        // whatClass="myProfile-title"
        style={{ margin: '65px 0 40px' }}
        text={getTranslatedText('settings.remove', lang)}
      />

      <p className="delete-text">
        {getTranslatedText('settings.describe', lang)}{' '}
        <Link to={`${route.userInfo}${route.myProfile}`}>
          &nbsp;{getTranslatedText('settings.profile', lang)}
        </Link>
      </p>

      <div className="btn-wrapper">
        <Button
          width="248px"
          whatClass="btn-profile"
          text={getTranslatedText('button.remove', lang)}
        />
      </div>
    </>
  );
};

export default memo(MySettings);
