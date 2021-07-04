import React, { useContext, useState } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { Link } from 'react-router-dom';
import { Formik } from 'formik';
import * as yup from 'yup';

import SpinnerForAuthBtn from '../../../../components/common/spinner/spinnerForAuthBtn';
import TitleBigBlue from '../../../../components/common/title_Big_Blue/title_Big_Blue';
import Button from '../../../../components/common/button/Button';
import InputProfile from '../../components/inputProfile';
import { route } from '../../../../routes/routeConstants';
import { getTranslatedText } from '../../../../components/local/localisation';
import {
  EMAIL_REG_EXP,
  PASSWORD_ALT_CODE_EXP,
  PASSWORD_REG_EXP,
} from '../../../../config';

import { putEmailFetch, putPasswordFetch } from '../../../../REST/Resources';
import { putEmail, unauthorized } from '../../../../store/auth/slice';
import { ModalContext } from '../../../../components/common/pop-up';

import './mySettings.scss';

const MySettings = () => {
  const { openModal } = useContext(ModalContext);
  const dispatch = useDispatch();
  const [isFetchPass, setIsFetchPass] = useState(false);
  const [isFetchEmail, setIsFetchEmail] = useState(false);
  const { lang, email: currentEmail } = useSelector((state) => state.auth);

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
        whatClass={'myProfile-title'}
        text={getTranslatedText(`settings.changePassword`, lang)}
      />
      <Formik
        initialValues={initialPasswordValues}
        validateOnBlur
        validationSchema={validationPasswordSchema}
        onSubmit={async (values, onSubmitProps) => {
          setIsFetchPass(true);
          try {
            const { data } = await putPasswordFetch(values);
            openModal({
              title: getTranslatedText('popup.serverResponse', lang),
              children: <p>{data}</p>,
            });
            onSubmitProps.resetForm();
            setIsFetchPass(false);
          } catch (e) {
            setIsFetchPass(false);
            if (e.response.status === 401) {
              dispatch(unauthorized());
            }
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
                name={'oldPassword'}
                label={getTranslatedText(`settings.currentPassword`, lang)}
                type={'password'}
              />
              <InputProfile
                name={'newPassword'}
                label={getTranslatedText(`settings.newPassword`, lang)}
                type={'password'}
              />
              <InputProfile
                name={'confirmNewPassword'}
                label={getTranslatedText(`settings.confirmPassword`, lang)}
                type={'password'}
              />
            </div>
            <Button
              text={
                isFetchPass ? (
                  <SpinnerForAuthBtn />
                ) : (
                  getTranslatedText(`button.save`, lang)
                )
              }
              whatClass={'btn-profile'}
              width={'248px'}
              height={'49px'}
              click={!errors.oldPassword ? handleSubmit : null}
              type={'submit'}
              disabling={!dirty && !isValid}
            />
          </>
        )}
      </Formik>

      <TitleBigBlue
        whatClass={'myProfile-title'}
        text={getTranslatedText(`settings.changeEmail`, lang)}
      />
      <Formik
        initialValues={initialEmailValues}
        validateOnBlur
        validationSchema={validationEmailSchema}
        onSubmit={async (values, onSubmitProps) => {
          const { newEmail, newEmailConfirmation } = values;
          setIsFetchEmail(true);
          try {
            const { data } = await putEmailFetch({
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
            if (e.response.status === 401) {
              dispatch(unauthorized());
            }
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
                name={'oldEmail'}
                label={getTranslatedText(`settings.oldEmail`, lang)}
                type={'email'}
                value={currentEmail}
                readOnly
              />
              <InputProfile
                name={'newEmail'}
                label={getTranslatedText(`settings.newEmail`, lang)}
                type={'email'}
              />
              <InputProfile
                name={'newEmailConfirmation'}
                label={getTranslatedText(`settings.confirmEmail`, lang)}
                type={'email'}
              />
            </div>
            <Button
              text={
                isFetchEmail ? (
                  <SpinnerForAuthBtn />
                ) : (
                  getTranslatedText(`button.saveEmail`, lang)
                )
              }
              whatClass={'btn-profile e-mail-button'}
              width={'300px'}
              height={'49px'}
              click={!errors.newEmail ? handleSubmit : null}
              type={'submit'}
              disabling={!isValid && !dirty}
            />
          </>
        )}
      </Formik>
      <TitleBigBlue
        whatClass={'myProfile-title'}
        text={getTranslatedText(`settings.remove`, lang)}
      />
      <p className="delete-text">
        {getTranslatedText(`settings.describe`, lang)}{' '}
        <Link to={`${route.userInfo}${route.myProfile}`}>
          &nbsp;{getTranslatedText(`settings.profile`, lang)}
        </Link>
      </p>
      <div className={'btn-wrapper'}>
        <Button
          text={getTranslatedText(`button.remove`, lang)}
          whatClass={'btn-profile'}
          width={'248px'}
        />
      </div>
    </>
  );
};

export default React.memo(MySettings);
