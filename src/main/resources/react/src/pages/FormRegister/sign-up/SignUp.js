import React, { useState } from 'react';
import { useSelector } from 'react-redux';
import { useHistory } from 'react-router-dom';
import { Formik } from 'formik';
import * as yup from 'yup';

import InputForAuth from '../../../components/common/input';
import CheckBox from '../../../components/common/checkbox';
import Button from '../../../components/common/buttons/button/Button';
import { getTranslatedText } from '../../../components/local/localisation';
import {
  NO_SPACE,
  EMAIL_REG_EXP,
  PASSWORD_REG_EXP,
  PASSWORD_ALT_CODE_EXP,
  USERNAME_ALT_CODE_EXP,
} from '../../../config';
import { postAuthRegister } from '../../../REST/Resources';
import { route } from '../../../routes/routeConstants';

import SpinnerForAuthBtn from '../../../components/common/spinner/spinnerForAuthBtn';
import { Extra } from '../sign-in/loginStyle';

const SignUp = () => {
  const history = useHistory();
  const { lang } = useSelector((state) => state.auth);
  const [loading, setLoading] = useState(false);
  const [checkbox, setCheckbox] = useState(false);

  const changeCheckBox = () => {
    setCheckbox((prev) => !prev);
  };

  const validationRegisterSchema = yup.object().shape({
    email: yup
      .string()
      .email(getTranslatedText('errors.invalidEmailFormat', lang))
      .required(getTranslatedText('errors.requireField', lang))
      .matches(EMAIL_REG_EXP, getTranslatedText('errors.emailMatch', lang))
      .matches(NO_SPACE, getTranslatedText('errors.noSpace', lang))
      .default(() => ''),
    username: yup
      .string()
      .required(getTranslatedText('errors.requireField', lang))
      .min(2, getTranslatedText('errors.min2', lang))
      .max(50, getTranslatedText('errors.max50', lang))
      .matches(
        USERNAME_ALT_CODE_EXP,
        getTranslatedText('errors.altCodeMatch', lang)
      )
      .matches(NO_SPACE, getTranslatedText('errors.noSpace', lang))
      .default(() => ''),
    password: yup
      .string()
      .required(getTranslatedText('errors.requireField', lang))
      .min(8, getTranslatedText('errors.min8', lang))
      .max(30, getTranslatedText('errors.max30', lang))
      .matches(
        PASSWORD_REG_EXP,
        getTranslatedText('errors.passwordMatch', lang)
      )
      .matches(
        PASSWORD_ALT_CODE_EXP,
        getTranslatedText('errors.passwordMatch', lang)
      )
      .matches(NO_SPACE, getTranslatedText('errors.noSpace', lang))
      .default(() => ''),
    confirmPassword: yup
      .string()
      .oneOf(
        [yup.ref('password')],
        getTranslatedText('errors.passwordMismatch', lang)
      )
      .required(getTranslatedText('errors.requireField', lang))
      .default(() => ''),
  });
  const initialRegisterValues = validationRegisterSchema.cast({});

  return (
    <form>
      <Formik
        initialValues={initialRegisterValues}
        validationSchema={validationRegisterSchema}
        validateOnBlur
        onSubmit={async (dataFormik, onSubmitProps) => {
          setLoading(true);
          try {
            await postAuthRegister(dataFormik);
            setLoading(false);
            history.push(route.login);
          } catch (err) {
            setLoading(false);
            if (err.response.status === 400) {
              const { error: message } = err.response.data;
              const field =
                message.includes('login') ||
                message.includes('логіном') ||
                message.includes('имя')
                  ? 'username'
                  : 'email';
              onSubmitProps.setErrors({ [field]: message });
            }
          }
        }}
      >
        {({ errors, isSubmitting, handleSubmit, isValid, dirty }) => {
          return (
            <>
              <div>
                <InputForAuth
                  text={getTranslatedText('auth.regEmail', lang)}
                  name="email"
                  type="email"
                />
                <InputForAuth
                  text={getTranslatedText('auth.regLogin', lang)}
                  name="username"
                  type="text"
                />
                <InputForAuth
                  text={getTranslatedText('auth.regPassword', lang)}
                  name="password"
                  type="password"
                />
                <InputForAuth
                  text={getTranslatedText('auth.regConfirm', lang)}
                  name="confirmPassword"
                  type="password"
                />
              </div>
              <Extra>
                <CheckBox
                  text={getTranslatedText('auth.agreement', lang)}
                  margin="0 0 44px 0"
                  fontSize="14px"
                  checked={checkbox}
                  click={changeCheckBox}
                />
              </Extra>
              <Button
                text={
                  loading ? (
                    <SpinnerForAuthBtn />
                  ) : (
                    getTranslatedText('auth.signUp', lang)
                  )
                }
                mb="44px"
                bold
                type="submit"
                lHeight="24px"
                width="222px"
                height="48px"
                disabling={!checkbox || isSubmitting || (!dirty && !isValid)}
                click={!errors.email || !errors.username ? handleSubmit : null}
              />
            </>
          );
        }}
      </Formik>
    </form>
  );
};

export default SignUp;
