import { useState } from 'react';
import { Formik } from 'formik';
import { useNavigate } from 'react-router-dom';
import { CheckBox } from '@wolshebnik/obminyashka-components';

import api from 'REST/Resources';
import { GoogleSvg } from 'assets/icons';
import { route } from 'routes/routeConstants';
import { Button, InputForAuth } from 'components/common';
import { getTranslatedText } from 'components/local/localization';

import { validationSchema } from './config';
import { Extra, WrapperButton, Form } from '../sign-in/styles';

const SignUp = () => {
  const navigate = useNavigate();
  const [loading, setLoading] = useState(false);

  const initialValues = validationSchema.cast({});

  const onSubmit = async (values, onSubmitProps) => {
    setLoading(true);

    try {
      await api.auth.postAuthRegister(values);
      navigate(route.login);
    } catch (err) {
      if (err.response.status === 409) {
        const { error: message } = err.response.data;
        onSubmitProps.setErrors({ username: message });
      }
    } finally {
      setLoading(false);
    }
  };

  return (
    <Form>
      <Formik
        validateOnBlur
        onSubmit={onSubmit}
        initialValues={initialValues}
        validationSchema={validationSchema}
      >
        {({
          dirty,
          errors,
          values,
          isValid,
          isSubmitting,
          handleSubmit,
          setFieldValue,
        }) => (
          <>
            <div>
              <InputForAuth
                name="email"
                type="text"
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
                gap={22}
                fontSize={14}
                name="agreement"
                margin="0 0 44px 0"
                checked={values.agreement}
                style={{ paddingRight: '10px' }}
                onChange={() => setFieldValue('agreement', !values.agreement)}
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
                disabling={!dirty || !isValid || isSubmitting}
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
