import { useState } from 'react';
import { Form, Formik } from 'formik';
import { useNavigate } from 'react-router-dom';
import { Button, CheckBox, Icon, InputField } from 'obminyashka-components';

import api from 'REST/Resources';
import { route } from 'routes/routeConstants';
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
            <Styles.WrapperInputSingUp>
              <InputField
                type="text"
                name="email"
                inputGap="3px"
                label={getTranslatedText('auth.regEmail')}
              />

              <InputField
                type="text"
                inputGap="3px"
                name="username"
                label={getTranslatedText('auth.regLogin')}
              />

              <InputField
                inputGap="3px"
                name="password"
                type="password"
                label={getTranslatedText('auth.regPassword')}
              />

              <InputField
                inputGap="3px"
                type="password"
                name="confirmPassword"
                label={getTranslatedText('auth.regConfirm')}
              />
            </Styles.WrapperInputSingUp>

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
