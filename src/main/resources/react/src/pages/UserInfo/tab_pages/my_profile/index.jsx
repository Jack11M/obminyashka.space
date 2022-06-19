import React, { useContext, useEffect, useState } from 'react';
import { FieldArray, Formik } from 'formik';
import { useDispatch, useSelector } from 'react-redux';

import api from 'REST/Resources';
import { fetchUser, getProfile, putUserToStore } from 'store/profile/slice';
import { getTranslatedText } from 'components/local/localization';
import { ModalContext, TitleBigBlue, Button } from 'components/common';

import { validationUserSchema } from './config';
import InputProfile from '../../components/inputProfile';
import InputGender from '../../components/inputProfile/inputGender';
import ButtonsAddRemoveChild from '../../components/buttonsAddRemoveChild';

import './myProfile.scss';

const MyProfile = () => {
  const { openModal } = useContext(ModalContext);
  const dispatch = useDispatch();
  const { firstName, lastName, children, phones } = useSelector(getProfile);
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

  const validationSchema = validationUserSchema({
    firstName,
    lastName,
    phoneForInitial,
  });

  const initialUserValues = validationUserSchema({
    firstName,
    lastName,
    phoneForInitial,
  }).cast({});

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
        enableReinitialize
        onSubmit={handleUserSubmit}
        initialValues={initialUserValues}
        validationSchema={validationSchema}
      >
        {({ values, errors, handleSubmit }) => (
          <>
            <TitleBigBlue
              whatClass="myProfile-title"
              text={getTranslatedText('ownInfo.aboutMe')}
            />

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
                <>
                  {values.phones.map((phone, index, arr) => {
                    const lastIndex = arr.length - 1;
                    const biggerThanStartIndex = arr.length > 1;
                    const maxArray = index < 2;
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
                            addRemove="add"
                            className="add-field"
                            text={getTranslatedText('button.addField')}
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
            <div className="block-child" key={String(`${idx}_child`)}>
              <InputProfile
                id={idx}
                type="date"
                onChange={null}
                name="birthDate"
                value={child.birthDate}
                label={getTranslatedText('ownInfo.dateOfBirth')}
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
