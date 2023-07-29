/* eslint-disable */
// @ts-nocheck
// TODO: fix typescript
import { useState } from "react";
import { Form, Formik } from "formik";
import { useDispatch } from "react-redux";
import { useLocation, useNavigate } from "react-router-dom";
import { ButtonNew, CheckBox, Icon, InputField } from "obminyashka-components";

import { route } from "src/routes/routeConstants";
import { putUserThunk } from "src/store/auth/thunk";
import { getTranslatedText } from "src/components/local/localization";

import * as Styles from "./styles";
import { validationSchema } from "./config";

const Login = () => {
  const dispatch = useDispatch();
  const navigate = useNavigate();
  const location = useLocation();

  const initialValues = validationSchema.cast({});
  const fromPage = location.state?.from?.pathname || "/";

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
            <Styles.WrapperInputSingIn>
              <InputField
                type="text"
                inputGap="3px"
                inputHeight="50px"
                name="usernameOrEmail"
                label={getTranslatedText("auth.logEmail")}
              />

              <InputField
                inputGap="3px"
                name="password"
                type="password"
                inputHeight="50px"
                label={getTranslatedText("auth.logPassword")}
              />
            </Styles.WrapperInputSingIn>

            <Styles.Extra>
              <CheckBox
                gap={22}
                fontSize={14}
                margin="0 0 44px 0"
                name="isLocalStorage"
                checked={values.isLocalStorage}
                text={getTranslatedText("auth.remember")}
                onChange={() =>
                  setFieldValue("isLocalStorage", !values.isLocalStorage)
                }
              />

              <Styles.ExtraLink to={`${route.login}/${route.signUp}`}>
                {getTranslatedText("auth.noLogin")}
              </Styles.ExtraLink>
            </Styles.Extra>

            <Styles.WrapperButtons>
              <ButtonNew
                bold
                type="submit"
                animated="true"
                isLoading={loading}
                disabled={!isValid && !dirty}
                text={getTranslatedText("button.enter")}
              />

              <Styles.FirstText>АБО</Styles.FirstText>

              <Styles.SecondText>Войти через соц сети</Styles.SecondText>

              <Styles.ButtonsRegistration>
                <ButtonNew
                  square="true"
                  styleType="outline"
                  icon={<Icon.FbRegistration />}
                />
                <ButtonNew
                  square="true"
                  styleType="outline"
                  icon={<Icon.AppleRegistration />}
                />
                <ButtonNew
                  square="true"
                  styleType="outline"
                  icon={<Icon.GoogleRegistration />}
                />
              </Styles.ButtonsRegistration>
            </Styles.WrapperButtons>
          </Form>
        )}
      </Formik>
    </Styles.Wrapper>
  );
};

export { Login };
