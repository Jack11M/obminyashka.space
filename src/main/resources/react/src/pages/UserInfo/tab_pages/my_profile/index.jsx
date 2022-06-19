import React, { useContext, useEffect, useState } from 'react';
import * as yup from 'yup';
import { FieldArray, Formik } from 'formik';
import { useDispatch, useSelector } from 'react-redux';

import api from 'REST/Resources';
import { NAME_REG_EXP, NO_SPACE, PHONE_REG_EXP } from 'config';
import { fetchUser, putUserToStore } from 'store/profile/slice';
import { getTranslatedText } from 'components/local/localization';
import { ModalContext, TitleBigBlue, Button } from 'components/common';

import InputProfile from '../../components/inputProfile';
import InputGender from '../../components/inputProfile/inputGender';
import ButtonsAddRemoveChild from '../../components/buttonsAddRemoveChild';

import './myProfile.scss';

const MyProfile = () => {
  const { openModal } = useContext(ModalContext);
  const dispatch = useDispatch();
  const { firstName, lastName, children, phones } = useSelector(
    (state) => state.profileMe
  );
  const [aboutLoading, setAboutLoading] = useState(false);

  const phoneForInitial =
    (!phones?.length && ['']) || phones?.map((phone) => phone.phoneNumber);

  useEffect(() => {
    dispatch(fetchUser());
  }, [dispatch]);

  const transformPhones = (array) =>
    array
      .map((phone) => ({
        defaultPhone: false,
        phoneNumber: phone,
      }))
      .filter((phone) => phone.phoneNumber !== '');

  const errorMessage = (validatedObject, message) =>
    validatedObject.value.length > 0 ? message : undefined;

  const validationUserSchema = yup.object().shape({
    firstName: yup
      .string()
      .min(2, (obj) => errorMessage(obj, getTranslatedText('errors.min2')))
      .max(50, (obj) => errorMessage(obj, getTranslatedText('errors.max50')))
      .matches(NO_SPACE, (obj) =>
        errorMessage(obj, getTranslatedText('errors.noSpace'))
      )
      .matches(NAME_REG_EXP, (obj) =>
        errorMessage(obj, getTranslatedText('errors.nameMatch'))
      )
      .default(() => firstName),
    lastName: yup
      .string()
      .min(2, (obj) => errorMessage(obj, getTranslatedText('errors.min2')))
      .max(50, (obj) => errorMessage(obj, getTranslatedText('errors.max50')))
      .matches(NO_SPACE, (obj) =>
        errorMessage(obj, getTranslatedText('errors.noSpace'))
      )
      .matches(NAME_REG_EXP, (obj) =>
        errorMessage(obj, getTranslatedText('errors.nameMatch'))
      )
      .default(() => lastName),
    phones: yup
      .array()
      .of(
        yup
          .string()
          .matches(PHONE_REG_EXP, getTranslatedText('errors.phoneMatch'))
      )
      .default(() => phoneForInitial),
  });
  const initialUserValues = validationUserSchema.cast({});

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
        setAboutLoading(false);
        openModal({
          title: getTranslatedText('popup.errorTitle'),
          children: (
            <p style={{ textAlign: 'center' }}>
              {getTranslatedText('popup.notEmptyInput')}
            </p>
          ),
        });
        return;
      }
      const data = await api.fetchProfile.putUserInfo(newUserData);
      openModal({
        title: getTranslatedText('popup.serverResponse'),
        children: <p>{data}</p>,
      });
      setAboutLoading(false);
      dispatch(putUserToStore(newUserData));
    } catch (err) {
      setAboutLoading(false);
      if (err.response.status === 400) {
        const indexStart = err.response.data.error.indexOf(':') + 1;
        openModal({
          title: getTranslatedText('popup.serverResponse'),
          children: (
            <p style={{ textAlign: 'center' }}>
              {err.response.data.error.slice(indexStart)}
            </p>
          ),
        });
      }
    }
  };

  return (
    <>
      <Formik
        initialValues={initialUserValues}
        validationSchema={validationUserSchema}
        validateOnBlur
        enableReinitialize
        onSubmit={handleUserSubmit}
      >
        {({ values, errors, handleSubmit }) => (
          <>
            <TitleBigBlue
              whatClass="myProfile-title"
              text={getTranslatedText('ownInfo.aboutMe')}
            />
            <InputProfile
              label={getTranslatedText('ownInfo.firstName')}
              type="text"
              name="firstName"
            />
            <InputProfile
              label={getTranslatedText('ownInfo.lastName')}
              type="text"
              name="lastName"
            />
            <FieldArray name="phones">
              {({ push, remove }) => (
                <>
                  {values.phones.map((phone, index, arr) => {
                    const lastIndex = arr.length - 1;
                    const biggerThanStartIndex = arr.length > 1;
                    const maxArray = index < 2;
                    const errorField = errors.phones && errors.phones[index];
                    return (
                      <div
                        // eslint-disable-next-line react/no-array-index-key
                        key={`phones[${index}]`}
                        style={{ position: 'relative' }}
                      >
                        <InputProfile
                          label={getTranslatedText('ownInfo.phone')}
                          type="tel"
                          name={`phones[${index}]`}
                          placeholder="+38(123) 456-78-90"
                        />
                        {lastIndex === index && maxArray && (
                          <ButtonsAddRemoveChild
                            className="add-field"
                            text={getTranslatedText('button.addField')}
                            addRemove="add"
                            onClick={() => !!phone && !errorField && push('')}
                          />
                        )}
                        {biggerThanStartIndex && (
                          <ButtonsAddRemoveChild
                            className="remove-field"
                            onClick={() => remove(index)}
                          />
                        )}
                      </div>
                    );
                  })}
                </>
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

      <form>
        <div className="block-children">
          <TitleBigBlue
            whatClass="myProfile-title"
            text={getTranslatedText('ownInfo.children')}
          />
          {children.map((child, idx) => (
            // eslint-disable-next-line react/no-array-index-key
            <div className="block-child" key={`${idx}_child`}>
              <InputProfile
                id={idx}
                label={getTranslatedText('ownInfo.dateOfBirth')}
                type="date"
                name="birthDate"
                value={child.birthDate}
                onChange={null}
              />
              <InputGender gender={child.sex} id={idx} click={null} />
            </div>
          ))}
        </div>
        <Button
          width="248px"
          disabling={false}
          whatClass="btn-form-children"
          text={getTranslatedText('button.saveChanges')}
        />
      </form>
    </>
  );
};
export default React.memo(MyProfile);
