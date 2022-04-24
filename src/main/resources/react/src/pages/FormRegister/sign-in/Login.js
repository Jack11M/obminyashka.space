import React, { useState } from 'react';
import * as yup from 'yup';
import { Formik } from 'formik';
import { useDispatch, useSelector } from 'react-redux';
import { useNavigate, useLocation } from 'react-router-dom';

import { NO_SPACE } from 'config';
import { GoogleSvg } from 'assets/icons';
import { getLang } from 'store/auth/slice';
import { route } from 'routes/routeConstants';
import { putUserThunk } from 'store/auth/thunk';
import CheckBox from 'components/common/checkbox';
import InputForAuth from 'components/common/input';
import Button from 'components/common/buttons/button/Button';
import { getTranslatedText } from 'components/local/localization';

import { Extra, ExtraLink, WrapperButton } from './styles';

const Login = () => {
  const dispatch = useDispatch();
  const navigate = useNavigate();
  const location = useLocation();
  const lang = useSelector(getLang);
  const [checkbox, setCheckbox] = useState(false);
  const [loading, setLoading] = useState(false);

  const fromPage = location.state?.from?.pathname || '/';

  const changeCheckBox = () => {
    setCheckbox((prev) => !prev);
  };

  const validationLoginSchema = yup.object().shape({
    usernameOrEmail: yup
      .string()
      .required(getTranslatedText('errors.requireField', lang))
      .matches(NO_SPACE, getTranslatedText('errors.noSpace', lang))
      .default(() => ''),
    password: yup
      .string()
      .required(getTranslatedText('errors.requireField', lang))
      .matches(NO_SPACE, getTranslatedText('errors.noSpace', lang))
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
    <form>
      <Formik
        validateOnBlur
        enableReinitialize
        onSubmit={onSubmitHandler}
        initialValues={initialLoginValues}
        validationSchema={validationLoginSchema}
      >
        {({ errors, handleSubmit, isValid, dirty }) => {
          return (
            <>
              <div>
                <InputForAuth
                  text={getTranslatedText('auth.logEmail', lang)}
                  name="usernameOrEmail"
                  type="text"
                />
                <InputForAuth
                  text={getTranslatedText('auth.logPassword', lang)}
                  name="password"
                  type="password"
                />
              </div>

              <Extra>
                <CheckBox
                  text={getTranslatedText('auth.remember', lang)}
                  margin="0 0 44px 0"
                  fontSize="14px"
                  checked={checkbox}
                  click={changeCheckBox}
                />
                <ExtraLink to={`${route.login}/${route.signUp}`}>
                  {getTranslatedText('auth.noLogin', lang)}
                </ExtraLink>
              </Extra>

              <WrapperButton>
                <Button
                  text={getTranslatedText('button.enter', lang)}
                  bold
                  height="48px"
                  lHeight="24px"
                  mb="64px"
                  type="submit"
                  width="222px"
                  isLoading={loading}
                  disabling={!isValid && !dirty}
                  click={!errors.usernameOrEmail ? handleSubmit : null}
                />

                <Button
                  bold
                  mb="64px"
                  height="48px"
                  type="button"
                  width="175px"
                  lHeight="24px"
                  icon={<GoogleSvg />}
                  text={getTranslatedText('button.googleOAuth', lang)}
                  click={() =>
                    window.location.assign('/oauth2/authorization/google')
                  }
                />
              </WrapperButton>
            </>
          );
        }}
      </Formik>
    </form>
  );
};

export default Login;
