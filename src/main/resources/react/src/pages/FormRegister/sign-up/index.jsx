import { useState } from 'react';
import { Formik } from 'formik';
import { useNavigate } from 'react-router-dom';
import { Button } from '@wolshebnik/obminyashka-components';

import api from 'REST/Resources';
import { GoogleSvg } from 'assets/icons';
import { route } from 'routes/routeConstants';

import { getTranslatedText } from 'components/local/localization';
import { CheckBox, InputForAuth } from 'components/common';

import { validationRegisterSchema } from './config';
import { Extra, WrapperButton, Form } from '../sign-in/styles';

const SignUp = () => {
  const navigate = useNavigate();
  const [loading, setLoading] = useState(false);
  const [checkbox, setCheckbox] = useState(false);

  const changeCheckBox = () => {
    setCheckbox((prev) => !prev);
  };

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
                width={222}
                height={48}
                lHeight={24}
                type="submit"
                isLoading={loading}
                style={{ marginBottom: 44 }}
                text={getTranslatedText('auth.signUp')}
                disabling={!checkbox || !dirty || !isValid || isSubmitting}
                onClick={
                  !errors.email || !errors.username ? handleSubmit : null
                }
              />

              <Button
                bold
                height={48}
                width={222}
                lHeight={24}
                type="button"
                icon={<GoogleSvg />}
                style={{ marginBottom: 64 }}
                text={getTranslatedText('auth.signUp')}
                onClick={() =>
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
