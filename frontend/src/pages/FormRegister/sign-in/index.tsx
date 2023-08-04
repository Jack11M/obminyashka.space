import axios from "axios";
import { useState } from "react";
import { Form, Formik, FormikValues } from "formik";
import { useDispatch } from "react-redux";
import { useLocation, useNavigate } from "react-router-dom";
import { CheckBox, InputField } from "obminyashka-components";

import { putUserThunk } from "src/store/auth/thunk";
import { getTranslatedText } from "src/components/local/localization";

import * as Styles from "./styles";
import { FormikBagType } from "../types";
import { validationSchema } from "./config";
import { ButtonsWrapper } from "../buttons-registration";

const Login = ({ setTab }: { setTab: (num: number) => void }) => {
  const dispatch = useDispatch();
  const navigate = useNavigate();
  const location = useLocation();

  const initialValues = validationSchema.cast({});
  const fromPage = location.state?.from?.pathname || "/";

  const [loading, setLoading] = useState(false);

  const onSubmit = async (
    values: FormikValues,
    onSubmitProps: FormikBagType
  ) => {
    if (loading) return;
    setLoading(true);

    try {
      await dispatch(putUserThunk(values));
      navigate(fromPage, { replace: true });
    } catch (err) {
      setLoading(false);
      if (axios.isAxiosError(err)) {
        if (err.response?.status === 400) {
          onSubmitProps.setErrors({
            usernameOrEmail: err.response.data.error,
          });
        }
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

              <Styles.ExtraButton type="button" onClick={() => setTab(1)}>
                {getTranslatedText("auth.noLogin")}
              </Styles.ExtraButton>
            </Styles.Extra>

            <ButtonsWrapper />
          </Form>
        )}
      </Formik>
    </Styles.Wrapper>
  );
};

export { Login };
