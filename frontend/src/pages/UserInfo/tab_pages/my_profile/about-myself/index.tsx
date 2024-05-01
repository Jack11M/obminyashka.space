/* eslint-disable */
// @ts-nocheck
// TODO: fix typescript
import { useState } from "react";
import PhoneInput from "react-phone-input-2";
import { FieldArray, Form, Formik } from "formik";
import { useDispatch, useSelector } from "react-redux";
import { Button, Icon, showMessage, InputField } from "obminyashka-components";

import api from "src/REST/Resources";
import { getProfile, putUserToStore } from "src/store/profile/slice";
import { getTranslatedText } from "src/components/local/localization";

import { validationUserSchema, getInitialUser } from "./config";

import * as Styles from "../styles";
import "react-phone-input-2/lib/style.css";

const amount = 3;

const AboutMyself = () => {
  const dispatch = useDispatch();
  const profile = useSelector(getProfile);
  const { firstName, lastName, phones } = profile || {};

  const [aboutLoading, setAboutLoading] = useState(false);

  const phoneForInitial =
    (!phones?.length && [""]) || phones?.map((phone) => phone.phoneNumber);

  const transformPhones = (array) =>
    array
      .map((phone) => ({
        defaultPhone: false,
        phoneNumber: phone.replace(/-|\(|\)/gi, ""),
      }))
      .filter((phone) => phone.phoneNumber !== "");

  const onSubmit = async (dataFormik) => {
    setAboutLoading(true);
    try {
      const newUserData = {
        ...dataFormik,
        phones: transformPhones(dataFormik.phones),
      };
      if (
        !newUserData.lastName &&
        !newUserData.firstName &&
        !newUserData.phones.length
      ) {
        showMessage.success(getTranslatedText("popup.notEmptyInput"));
        return;
      }
      await api.profile.putUserInfo(newUserData);
      dispatch(putUserToStore(newUserData));
      showMessage.success(getTranslatedText("toastText.changedData"));
    } catch (err) {
      if (err.response?.status === 400) {
        const indexStart =
          err.response?.data?.error && err.response.data.error.indexOf(":") + 1;
        showMessage.error(err.response?.data?.error?.slice(indexStart));
        console.log(err.response?.data?.error?.slice(indexStart));
      }
    } finally {
      setAboutLoading(false);
    }
  };

  return (
    <Formik
      enableReinitialize
      onSubmit={onSubmit}
      validationSchema={validationUserSchema}
      initialValues={getInitialUser({ lastName, firstName, phoneForInitial })}
    >
      {({ values, errors, setFieldValue }) => (
        <Form>
          <Styles.WrapperInputWhitOutPhones>
            <InputField
              type="text"
              name="firstName"
              labelColor="black"
              inputMaxWidth="588px"
              inputFlexDirection="row"
              wrapperInputErrorWidth="415px"
              inputJustifyContent="space-between"
              label={getTranslatedText("ownInfo.firstName")}
            />

            <InputField
              type="text"
              name="lastName"
              labelColor="black"
              inputMaxWidth="588px"
              inputFlexDirection="row"
              wrapperInputErrorWidth="415px"
              inputJustifyContent="space-between"
              label={getTranslatedText("ownInfo.lastName")}
            />
          </Styles.WrapperInputWhitOutPhones>

          <FieldArray name="phones">
            {({ push, remove }) => (
              <Styles.WrapperInputPhones>
                {values.phones?.map((phone, index, arr) => {
                  const lastIndex = arr.length - 1;
                  const biggerThanStartIndex = arr.length > 1;
                  const maxArray = index + 1 !== amount;
                  const errorField = errors.phones && errors.phones[index];

                  return (
                    <Styles.WrapperInputAddPhones
                      key={String(`phones[${index}]`)}
                    >
                      <Styles.InputPhonesContainer>
                        <label htmlFor={`phones[${index}]`}>
                          {getTranslatedText("ownInfo.phone")}
                        </label>

                        <PhoneInput
                          country="ua"
                          enableSearch
                          name={`phones[${index}]`}
                          value={values.phones[index]}
                          onChange={(phone) => {
                            setFieldValue(`phones[${index}]`, phone);
                          }}
                        />
                      </Styles.InputPhonesContainer>

                      {lastIndex === index && maxArray && (
                        <Styles.WrapperAddButton>
                          <Button
                            gap={20}
                            width={34}
                            height={34}
                            outsideText
                            colorType="green"
                            icon={<Icon.Plus />}
                            text={getTranslatedText("button.addField")}
                            onClick={() => {
                              if (!!phone && !errorField) push("");
                              else {
                                showMessage.error(
                                  getTranslatedText("ownInfo.choosePhone")
                                );
                              }
                            }}
                          />
                        </Styles.WrapperAddButton>
                      )}

                      {biggerThanStartIndex && (
                        <Styles.WrapperDelButton>
                          <Button
                            gap={34}
                            width={34}
                            height={34}
                            outsideText
                            colorType="grey"
                            icon={<Icon.Plus />}
                            onClick={() => remove(index)}
                          />
                        </Styles.WrapperDelButton>
                      )}
                    </Styles.WrapperInputAddPhones>
                  );
                })}
              </Styles.WrapperInputPhones>
            )}
          </FieldArray>

          <Button
            width={248}
            type="submit"
            isLoading={aboutLoading}
            whatClass="btn-form-about-me"
            disabling={Object.keys(errors).length}
            text={getTranslatedText("button.saveChanges")}
          />
        </Form>
      )}
    </Formik>
  );
};

export { AboutMyself };
