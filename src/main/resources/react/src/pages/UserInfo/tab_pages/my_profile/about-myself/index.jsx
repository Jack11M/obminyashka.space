import { useState } from 'react';
import { toast } from 'react-toastify';
import { FieldArray, Formik } from 'formik';
import { useDispatch, useSelector } from 'react-redux';

import api from 'REST/Resources';
import { Button } from 'components/common';
import { getProfile, putUserToStore } from 'store/profile/slice';
import { getTranslatedText } from 'components/local/localization';

import { validationUserSchema } from './config';
import InputProfile from '../../../components/inputProfile';
import ButtonsAddRemoveChild from '../../../components/buttonsAddRemoveChild';

const amount = 3;

const AboutMyself = () => {
  const dispatch = useDispatch();
  const { firstName, lastName, phones } = useSelector(getProfile);

  const [aboutLoading, setAboutLoading] = useState(false);

  const phoneForInitial =
    (!phones?.length && ['']) || phones?.map((phone) => phone.phoneNumber);

  const transformPhones = (array) =>
    array
      .map((phone) => ({
        defaultPhone: false,
        phoneNumber: phone,
      }))
      .filter((phone) => phone.phoneNumber !== '');

  const validationSchema = validationUserSchema({
    firstName,
    lastName,
    phoneForInitial,
  });

  const initialUserValues = validationSchema.cast({});

  const handleUserSubmit = async (dataFormik) => {
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
        toast.success(getTranslatedText('popup.notEmptyInput'));
        return;
      }
      const data = await api.profile.putUserInfo(newUserData);
      dispatch(putUserToStore(newUserData));
      toast.success(data);
    } catch (err) {
      if (err.response?.status === 400) {
        const indexStart =
          err.response?.data?.error && err.response.data.error.indexOf(':') + 1;
        toast.success(err.response?.data?.error?.slice(indexStart));
      }
    } finally {
      setAboutLoading(false);
    }
  };

  return (
    <Formik
      enableReinitialize
      onSubmit={handleUserSubmit}
      initialValues={initialUserValues}
      validationSchema={validationSchema}
    >
      {({ values, errors, handleSubmit }) => (
        <>
          <InputProfile
            type="text"
            name="firstName"
            label={getTranslatedText('ownInfo.firstName')}
          />

          <InputProfile
            type="text"
            name="lastName"
            label={getTranslatedText('ownInfo.lastName')}
          />

          <FieldArray name="phones">
            {({ push, remove }) => (
              <div style={{ marginBottom: 55 }}>
                {values.phones.map((phone, index, arr) => {
                  const lastIndex = arr.length - 1;
                  const biggerThanStartIndex = arr.length > 1;
                  const maxArray = index + 1 !== amount;
                  const errorField = errors.phones && errors.phones[index];

                  return (
                    <div
                      key={String(`phones[${index}]`)}
                      style={{ position: 'relative' }}
                    >
                      <InputProfile
                        type="tel"
                        name={`phones[${index}]`}
                        placeholder="+38(123) 456-78-90"
                        label={getTranslatedText('ownInfo.phone')}
                      />

                      {lastIndex === index && maxArray && (
                        <ButtonsAddRemoveChild
                          add
                          text={getTranslatedText('button.addField')}
                          onClick={() => !!phone && !errorField && push('')}
                        />
                      )}

                      {biggerThanStartIndex && (
                        <ButtonsAddRemoveChild onClick={() => remove(index)} />
                      )}
                    </div>
                  );
                })}
              </div>
            )}
          </FieldArray>

          <Button
            width="248px"
            type="submit"
            click={handleSubmit}
            isLoading={aboutLoading}
            whatClass="btn-form-about-me"
            disabling={Object.keys(errors).length}
            text={getTranslatedText('button.saveChanges')}
          />
        </>
      )}
    </Formik>
  );
};

export { AboutMyself };
