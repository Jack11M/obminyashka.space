import { useState } from 'react';
import { Form, Formik } from 'formik';
import { useNavigate } from 'react-router-dom';
import { Button, CheckBox, Icon } from 'obminyashka-components';

import api from 'REST/Resources';
import { route } from 'routes/routeConstants';
import { InputForAuth } from 'components/common';
import { getTranslatedText } from 'components/local/localization';

import * as Styles from '../sign-in/styles';
import { validationSchema } from './config';

const SignUp = () => {
  const navigate = useNavigate();
  const [loading, setLoading] = useState(false);

  const initialValues = validationSchema.cast({});

  const onSubmit = async (values, onSubmitProps) => {
    setLoading(true);
    delete values.agreement;

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
    <Styles.Wrapper>
      <Formik
        validateOnBlur
        onSubmit={onSubmit}
        initialValues={initialValues}
        validationSchema={validationSchema}
      >
        {({ dirty, values, isValid, isSubmitting, setFieldValue }) => (
          <Form>
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

            <Styles.Extra>
              <CheckBox
                gap={22}
                fontSize={14}
                name="agreement"
                margin="0 0 44px 0"
                checked={values.agreement}
                style={{ paddingRight: '10px' }}
                text={getTranslatedText('auth.agreement')}
                onChange={() => setFieldValue('agreement', !values.agreement)}
              />
            </Styles.Extra>

            <Styles.WrapperButton>
              <Button
                bold
                width={222}
                height={48}
                lHeight={24}
                type="submit"
                isLoading={loading}
                style={{ marginBottom: 44 }}
                text={getTranslatedText('auth.signUp')}
                disabling={!dirty || !isValid || isSubmitting}
              />

              <Button
                bold
                height={48}
                width={222}
                lHeight={24}
                type="button"
                nativeIcon={false}
                icon={<Icon.Google />}
                style={{ marginBottom: 64 }}
                text={getTranslatedText('auth.signUp')}
                onClick={() =>
                  window.location.assign('/oauth2/authorization/google')
                }
              />
            </Styles.WrapperButton>
          </Form>
        )}
      </Formik>
    </Styles.Wrapper>
  );
};

export { SignUp };
