/* eslint-disable */
// @ts-nocheck
// TODO: fix typescript
import { useState } from "react";
import { Form, Formik } from "formik";
import { useNavigate } from "react-router-dom";
import {
  Icon,
  Images,
  CheckBox,
  ButtonNew,
  InputField,
  Responsive,
} from "obminyashka-components";

import api from "src/REST/Resources";
import { route } from "src/routes/routeConstants";
import { getTranslatedText } from "src/components/local/localization";

import * as StylesNative from "./styles";
import * as Styles from "../sign-in/styles";
import { validationSchema } from "./config";

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
        onSubmitProps.setErrors({ username: err.response.data });
      }
    } finally {
      setLoading(false);
    }
  };

  return (
    <StylesNative.Wrapper>
      <Responsive.Desktop>
        <StylesNative.SunRegistration
          src={Images.sunRegistration}
          alt="sun-registration"
        />
      </Responsive.Desktop>

      <StylesNative.FormikContainer>
        <Formik
          validateOnBlur
          onSubmit={onSubmit}
          initialValues={initialValues}
          validationSchema={validationSchema}
        >
          {({ dirty, values, isValid, isSubmitting, setFieldValue }) => (
            <Form>
              <StylesNative.WrapperInputSingUp>
                <InputField
                  type="text"
                  name="email"
                  inputGap="3px"
                  inputHeight="50px"
                  label={getTranslatedText("auth.regEmail")}
                />

                <InputField
                  type="text"
                  inputGap="3px"
                  name="username"
                  inputHeight="50px"
                  label={getTranslatedText("auth.regLogin")}
                />

                <InputField
                  inputGap="3px"
                  name="password"
                  type="password"
                  inputHeight="50px"
                  label={getTranslatedText("auth.regPassword")}
                />

                <InputField
                  inputGap="3px"
                  type="password"
                  inputHeight="50px"
                  name="confirmPassword"
                  label={getTranslatedText("auth.regConfirm")}
                />
              </StylesNative.WrapperInputSingUp>

              <Styles.Extra>
                <CheckBox
                  gap={22}
                  fontSize={14}
                  name="agreement"
                  margin="0 0 44px 0"
                  checked={values.agreement}
                  style={{ paddingRight: "10px" }}
                  text={getTranslatedText("auth.agreement")}
                  onChange={() => setFieldValue("agreement", !values.agreement)}
                />
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
      </StylesNative.FormikContainer>
    </StylesNative.Wrapper>
  );
};

export { SignUp };
