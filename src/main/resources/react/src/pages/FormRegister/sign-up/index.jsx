import { useState } from 'react';
import * as yup from 'yup';
import { Formik } from 'formik';
import { useNavigate } from 'react-router-dom';

import api from 'REST/Resources';
import { GoogleSvg } from 'assets/icons';
import { route } from 'routes/routeConstants';
import { getTranslatedText } from 'components/local/localization';
import { CheckBox, Button, InputForAuth } from 'components/common';
import {
  NO_SPACE,
  EMAIL_REG_EXP,
  PASSWORD_REG_EXP,
  PASSWORD_ALT_CODE_EXP,
  USERNAME_ALT_CODE_EXP,
} from 'config';

import { Extra, WrapperButton, Form } from '../sign-in/styles';

const SignUp = () => {
  const navigate = useNavigate();
  const [loading, setLoading] = useState(false);
  const [checkbox, setCheckbox] = useState(false);

  const changeCheckBox = () => {
    setCheckbox((prev) => !prev);
  };

  const validationRegisterSchema = yup.object().shape({
    email: yup
      .string()
      .email(getTranslatedText('errors.invalidEmailFormat'))
      .required(getTranslatedText('errors.requireField'))
      .matches(EMAIL_REG_EXP, getTranslatedText('errors.max129'))
      .matches(NO_SPACE, getTranslatedText('errors.noSpace'))
      .default(() => ''),
    username: yup
      .string()
      .required(getTranslatedText('errors.requireField'))
      .min(2, getTranslatedText('errors.min2'))
      .max(50, getTranslatedText('errors.max50'))
      .matches(USERNAME_ALT_CODE_EXP, getTranslatedText('errors.altCodeMatch'))
      .matches(NO_SPACE, getTranslatedText('errors.noSpace'))
      .default(() => ''),
    password: yup
      .string()
      .required(getTranslatedText('errors.requireField'))
      .min(8, getTranslatedText('errors.min8'))
      .max(30, getTranslatedText('errors.max30'))
      .matches(PASSWORD_REG_EXP, getTranslatedText('errors.passwordMatch'))
      .matches(PASSWORD_ALT_CODE_EXP, getTranslatedText('errors.passwordMatch'))
      .matches(NO_SPACE, getTranslatedText('errors.noSpace'))
      .default(() => ''),
    confirmPassword: yup
      .string()
      .oneOf(
        [yup.ref('password')],
        getTranslatedText('errors.passwordMismatch')
      )
      .required(getTranslatedText('errors.requireField'))
      .default(() => ''),
  });
  const initialRegisterValues = validationRegisterSchema.cast({});

  return (
    <Form>
      <Formik
        validateOnBlur
        initialValues={initialRegisterValues}
        validationSchema={validationRegisterSchema}
        onSubmit={async (dataFormik, onSubmitProps) => {
          setLoading(true);
          try {
            await api.auth.postAuthRegister(dataFormik);
            setLoading(false);
            navigate(route.login);
          } catch (err) {
            setLoading(false);
            if (err.response.status === 409) {
              const { error: message } = err.response.data;
              onSubmitProps.setErrors({ username: message });
            }
          }
        }}
      >
        {({ errors, isSubmitting, handleSubmit, isValid, dirty }) => (
          <>
            <div>
              <InputForAuth
                name="email"
                type="email"
                text={getTranslatedText('auth.regEmail')}
              />

              <InputForAuth
                type="text"
                name="username"
                text={getTranslatedText('auth.regLogin')}
              />

              <InputForAuth
                name="password"
                type="password"
                text={getTranslatedText('auth.regPassword')}
              />

              <InputForAuth
                type="password"
                name="confirmPassword"
                text={getTranslatedText('auth.regConfirm')}
              />
            </div>

            <Extra>
              <CheckBox
                fontSize="14px"
                checked={checkbox}
                margin="0 0 44px 0"
                click={changeCheckBox}
                text={getTranslatedText('auth.agreement')}
              />
            </Extra>

            <WrapperButton>
              <Button
                bold
                mb="44px"
                type="submit"
                width="222px"
                height="48px"
                lHeight="24px"
                isLoading={loading}
                text={getTranslatedText('auth.signUp')}
                disabling={!checkbox || isSubmitting || (!dirty && !isValid)}
                click={!errors.email || !errors.username ? handleSubmit : null}
              />

              <Button
                bold
                mb="64px"
                height="48px"
                type="button"
                width="222px"
                lHeight="24px"
                icon={<GoogleSvg />}
                text={getTranslatedText('auth.signUp')}
                click={() =>
                  window.location.assign('/oauth2/authorization/google')
                }
              />
            </WrapperButton>
          </>
        )}
      </Formik>
    </Form>
  );
};

export default SignUp;
