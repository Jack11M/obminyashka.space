import axios from "axios";
import { useState } from "react";
import { Form, Formik, FormikValues } from "formik";
import {
  Images,
  CheckBox,
  InputField,
  Responsive,
} from "obminyashka-components";

import api from "src/REST/Resources";
import { getTranslatedText } from "src/components/local/localization";

import * as StylesNative from "./styles";
import { FormikBagType } from "../types";
import * as Styles from "../sign-in/styles";
import { validationSchema } from "./config";
import { ButtonsWrapper } from "../buttons-registration";

const SignUp = ({ setTab }: { setTab: (num: number) => void }) => {
  const [loading, setLoading] = useState(false);

  const initialValues = validationSchema.cast({});

  const onSubmit = async (
    values: FormikValues,
    onSubmitProps: FormikBagType
  ) => {
    setLoading(true);
    delete values.agreement;

    try {
      await api.auth.postAuthRegister(values);
      setTab(0);
    } catch (err) {
      if (axios.isAxiosError(err)) {
        if (err.response?.status === 409) {
          onSubmitProps.setErrors({ username: err.response.data });
        }
      }
    } finally {
      setLoading(false);
    }
  };

  return (
    <StylesNative.Wrapper>
      <Responsive.Desktop>
        <StylesNative.SunRegistration
          alt="sun-registration"
          src={Images.sunRegistration}
        />
      </Responsive.Desktop>

      <StylesNative.FormikContainer>
        <Formik
          validateOnBlur
          onSubmit={onSubmit}
          initialValues={initialValues}
          validationSchema={validationSchema}
        >
          {({ values, setFieldValue }) => (
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
                  autoComplete="off"
                  inputHeight="50px"
                  label={getTranslatedText("auth.regLogin")}
                />

                <InputField
                  inputGap="3px"
                  name="password"
                  type="password"
                  autoComplete="off"
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
                  checked={values.agreement}
                  style={{ alignItems: "center" }}
                  text={getTranslatedText("auth.agreement")}
                  onChange={() => setFieldValue("agreement", !values.agreement)}
                />
              </Styles.Extra>

              <ButtonsWrapper register isLoading={loading} />
            </Form>
          )}
        </Formik>
      </StylesNative.FormikContainer>
    </StylesNative.Wrapper>
  );
};

export { SignUp };
