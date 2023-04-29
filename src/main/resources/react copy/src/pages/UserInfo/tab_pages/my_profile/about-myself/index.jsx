import { useState } from 'react';
import { FieldArray, Form, Formik } from 'formik';
import { useDispatch, useSelector } from 'react-redux';
import { Button, Icon, showMessage, InputField } from 'obminyashka-components';

import api from 'REST/Resources';
import { getProfile, putUserToStore } from 'store/profile/slice';
import { getTranslatedText } from 'components/local/localization';

import * as Styles from '../styles';
import { validationUserSchema, getInitialUser } from './config';

const amount = 3;

const AboutMyself = () => {
  const dispatch = useDispatch();
  const profile = useSelector(getProfile);
  const { firstName, lastName, phones } = profile || {};

  const [aboutLoading, setAboutLoading] = useState(false);

  const phoneForInitial =
    (!phones?.length && ['']) || phones?.map((phone) => phone.phoneNumber);

  const transformPhones = (array) =>
    array
      .map((phone) => ({
        defaultPhone: false,
        phoneNumber: phone.replace(/-|\(|\)/gi, ''),
      }))
      .filter((phone) => phone.phoneNumber !== '');

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
        showMessage.success(getTranslatedText('popup.notEmptyInput'));
        return;
      }
      await api.profile.putUserInfo(newUserData);
      dispatch(putUserToStore(newUserData));
      showMessage.success(getTranslatedText('toastText.changedData'));
    } catch (err) {
      if (err.response?.status === 400) {
        const indexStart =
          err.response?.data?.error && err.response.data.error.indexOf(':') + 1;
        showMessage.error(err.response?.data?.error?.slice(indexStart));
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
      {({ values, errors }) => (
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
              label={getTranslatedText('ownInfo.firstName')}
            />

            <InputField
              type="text"
              name="lastName"
              labelColor="black"
              inputMaxWidth="588px"
              inputFlexDirection="row"
              wrapperInputErrorWidth="415px"
              inputJustifyContent="space-between"
              label={getTranslatedText('ownInfo.lastName')}
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
                      <InputField
                        type="tel"
                        labelColor="black"
                        inputMaxWidth="588px"
                        inputFlexDirection="row"
                        name={`phones[${index}]`}
                        wrapperInputErrorWidth="415px"
                        placeholder="+380(12)345-67-89"
                        inputJustifyContent="space-between"
                        label={getTranslatedText('ownInfo.phone')}
                      />

                      {lastIndex === index && maxArray && (
                        <Styles.WrapperAddButton>
                          <Button
                            gap={20}
                            width={34}
                            height={34}
                            outsideText
                            colorType="green"
                            icon={<Icon.Plus />}
                            text={getTranslatedText('button.addField')}
                            onClick={() => {
                              if (!!phone && !errorField) push('');
                              else {
                                showMessage.error(
                                  getTranslatedText('ownInfo.choosePhone')
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
            text={getTranslatedText('button.saveChanges')}
          />
        </Form>
      )}
    </Formik>
  );
};

export { AboutMyself };
