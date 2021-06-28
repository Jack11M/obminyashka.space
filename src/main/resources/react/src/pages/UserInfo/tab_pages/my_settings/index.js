import React, { useState } from 'react';
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

import './mySettings.scss';
import { putEmailFetch, putPasswordFetch } from '../../../../REST/Resources';
import { putEmail, unauthorized } from '../../../../store/auth/slice';

const MySettings = () => {
  const dispatch = useDispatch();
  const [isFetchPass, setIsFetchPass] = useState(false);
  const [isFetchEmail, setIsFetchEmail] = useState(false);
  const { lang, email: currentEmail } = useSelector((state) => state.auth);

  const validationPasswordSchema = yup.object().shape({
    oldPassword: yup
      .string()
      .min(8, 'must be at least 8 characters')
      .max(30, 'must be no more than 30 characters')
      .matches(PASSWORD_REG_EXP, 'Большие и малые латинские буквы и цифры.')
      .matches(PASSWORD_ALT_CODE_EXP, 'Не должно быть Alt code')
      .required('Поле обязательно')
      .default(() => ''),
    newPassword: yup
      .string()
      .min(8, 'must be at least 8 characters')
      .max(30, 'must be at least 30 characters')
      .matches(PASSWORD_REG_EXP, 'Большие и малые латинские буквы и цифры.')
      .matches(PASSWORD_ALT_CODE_EXP, 'Не должно быть Alt code')
      .notOneOf([yup.ref('oldPassword'), false], 'Пароль совпадает с текущим')
      .required('Поле обязательно')
      .default(() => ''),
    confirmNewPassword: yup
      .string()
      .oneOf([yup.ref('newPassword')], 'Пароли не совпадают')
      .required('Поле обязательно')
      .default(() => ''),
  });

  const validationEmailSchema = yup.object().shape({
    newEmail: yup
      .string()
      .notOneOf([currentEmail], 'Email совпадает со старым')
      .matches(EMAIL_REG_EXP, 'must be at least 129 characters')
      .email('Не верный формат Email')
      .required('Поле обязательно')
      .default(() => ''),
    newEmailConfirmation: yup
      .string()
      .oneOf([yup.ref('newEmail')], 'Email не совпадает')
      .email('Не верный формат Email')
      .required('Поле обязательно')
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
            await putPasswordFetch(values);
            // create POPup ant insert  data message, Password been changed
            onSubmitProps.resetForm();
          } catch (e) {
            if (e.response.status === 401) {
              dispatch(unauthorized());
            }
            if (e.response.status === 400) {
              onSubmitProps.setErrors({ oldPassword: e.response.data.error });
            }
          } finally {
            setIsFetchPass(false);
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
            await putEmailFetch({ newEmail, newEmailConfirmation });
            // create POPup ant insert  data message, Email been changed
            dispatch(putEmail(newEmail));
            onSubmitProps.resetForm();
          } catch (e) {
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
          } finally {
            setIsFetchEmail(false);
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
                  getTranslatedText(`button.codeConfirm`, lang)
                )
              }
              whatClass={'btn-profile e-mail-button'}
              width={'363px'}
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
