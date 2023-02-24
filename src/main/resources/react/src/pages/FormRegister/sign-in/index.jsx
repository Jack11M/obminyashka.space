import { useState } from 'react';
import { Formik, Form } from 'formik';
import { useDispatch } from 'react-redux';
import { useNavigate, useLocation } from 'react-router-dom';
import { Icon, CheckBox } from '@wolshebnik/obminyashka-components';

import { route } from 'routes/routeConstants';
import { putUserThunk } from 'store/auth/thunk';
import { Button, InputForAuth } from 'components/common';
import { getTranslatedText } from 'components/local/localization';

import * as Styles from './styles';
import { validationSchema } from './config';

const Login = () => {
  const dispatch = useDispatch();
  const navigate = useNavigate();
  const location = useLocation();

  const initialValues = validationSchema.cast({});
  const fromPage = location.state?.from?.pathname || '/';

  const [loading, setLoading] = useState(false);

  const onSubmit = async (values, onSubmitProps) => {
    if (loading) return;
    setLoading(true);

    try {
      await dispatch(putUserThunk(values));
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
    <Styles.Wrapper>
      <Formik
        validateOnBlur
        onSubmit={onSubmit}
        initialValues={initialValues}
        validationSchema={validationSchema}
      >
        {({ isValid, dirty, values, setFieldValue }) => (
          <Form>
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

            <Styles.Extra>
              <CheckBox
                gap={22}
                fontSize={14}
                margin="0 0 44px 0"
                name="isLocalStorage"
                checked={values.isLocalStorage}
                text={getTranslatedText('auth.remember')}
                onChange={() =>
                  setFieldValue('isLocalStorage', !values.isLocalStorage)
                }
              />

              <Styles.ExtraLink to={`${route.login}/${route.signUp}`}>
                {getTranslatedText('auth.noLogin')}
              </Styles.ExtraLink>
            </Styles.Extra>

            <Styles.WrapperButton>
              <Button
                bold
                mb="64px"
                height="48px"
                type="submit"
                width="222px"
                lHeight="24px"
                isLoading={loading}
                disabling={!isValid && !dirty}
                text={getTranslatedText('button.enter')}
              />

              <Button
                bold
                mb="64px"
                height="48px"
                type="button"
                width="175px"
                lHeight="24px"
                icon={<Icon.Google />}
                text={getTranslatedText('button.googleOAuth')}
                click={() =>
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

export { Login };
