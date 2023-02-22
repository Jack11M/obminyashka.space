import { useState } from 'react';
import * as yup from 'yup';
import { Formik } from 'formik';
import { useDispatch } from 'react-redux';
import { useNavigate, useLocation } from 'react-router-dom';
import { Button, CheckBox } from '@wolshebnik/obminyashka-components';

import { NO_SPACE } from 'config';
import { GoogleSvg } from 'assets/icons';
import { route } from 'routes/routeConstants';

import { putUserThunk } from 'store/auth/thunk';
import { InputForAuth } from 'components/common';
import { getTranslatedText } from 'components/local/localization';

import { Extra, ExtraLink, WrapperButton, Form } from './styles';

const Login = () => {
  const dispatch = useDispatch();
  const navigate = useNavigate();
  const location = useLocation();
  const [checkbox, setCheckbox] = useState(false);
  const [loading, setLoading] = useState(false);

  const fromPage = location.state?.from?.pathname || '/';

  const changeCheckBox = () => {
    setCheckbox((prev) => !prev);
  };

  const validationLoginSchema = yup.object().shape({
    usernameOrEmail: yup
      .string()
      .required(getTranslatedText('errors.requireField'))
      .matches(NO_SPACE, getTranslatedText('errors.noSpace'))
      .default(() => ''),
    password: yup
      .string()
      .required(getTranslatedText('errors.requireField'))
      .matches(NO_SPACE, getTranslatedText('errors.noSpace'))
      .default(() => ''),
  });
  const initialLoginValues = validationLoginSchema.cast({});

  const onSubmitHandler = async (values, onSubmitProps) => {
    if (loading) return;
    setLoading(true);
    try {
      await dispatch(putUserThunk(values, checkbox));
      navigate(fromPage, { replace: true });
    } catch (err) {
      setLoading(false);
      if (err.response.status === 400) {
        onSubmitProps.setErrors({
          usernameOrEmail: err.response.data.error,
        });
      }
    }
  };

  return (
    <Form>
      <Formik
        validateOnBlur
        enableReinitialize
        onSubmit={onSubmitHandler}
        initialValues={initialLoginValues}
        validationSchema={validationLoginSchema}
      >
        {({ errors, handleSubmit, isValid, dirty }) => (
          <>
            <div>
              <InputForAuth
                type="text"
                name="usernameOrEmail"
                text={getTranslatedText('auth.logEmail')}
              />

              <InputForAuth
                name="password"
                type="password"
                text={getTranslatedText('auth.logPassword')}
              />
            </div>

            <Extra>
              <CheckBox
                gap={22}
                fontSize={14}
                checked={checkbox}
                margin="0 0 44px 0"
                onChange={changeCheckBox}
                text={getTranslatedText('auth.remember')}
              />

              <ExtraLink to={`${route.login}/${route.signUp}`}>
                {getTranslatedText('auth.noLogin')}
              </ExtraLink>
            </Extra>

            <WrapperButton>
              <Button
                bold
                width={222}
                lHeight={24}
                type="submit"
                isLoading={loading}
                style={{ marginBottom: 64 }}
                disabled={!isValid && !dirty}
                text={getTranslatedText('button.enter')}
                onClick={!errors.usernameOrEmail ? handleSubmit : null}
              />

              <Button
                bold
                width={175}
                lHeight={24}
                type="button"
                icon={<GoogleSvg />}
                style={{ marginBottom: 64, height: 48 }}
                text={getTranslatedText('button.googleOAuth')}
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

export default Login;
