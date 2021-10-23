import React, { useContext, useEffect, useState } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { FieldArray, Formik } from 'formik';
import * as yup from 'yup';

import InputProfile from '../../components/inputProfile';
import Button from 'components/common/buttons/button/Button';
import { NAME_REG_EXP, NO_SPACE, PHONE_REG_EXP } from 'config';
import { fetchUser, putUserToStore } from 'store/profile/slice';

import TitleBigBlue from 'components/common/title_Big_Blue';
import { getTranslatedText } from 'components/local/localisation';
import ButtonsAddRemoveChild from 'pages/UserInfo/components/buttonsAddRemoveChild/buttonsAddRemoveChild';
import InputGender from '../../components/inputProfile/inputGender';
import { putUserInfo } from 'REST/Resources';
import { ModalContext } from 'components/common/pop-up';

import './myProfile.scss';

const MyProfile = () => {
  const { openModal } = useContext(ModalContext);
  const dispatch = useDispatch();
  const { lang } = useSelector((state) => state.auth);
  const { firstName, lastName, children, phones } = useSelector(
    (state) => state.profileMe
  );
  const [aboutLoading, setAboutLoading] = useState(false);

  const phoneForInitial =
    (!phones.length && ['']) || phones.map((phone) => phone.phoneNumber);

  useEffect(() => {
    dispatch(fetchUser());
  }, [dispatch]);

  const transformPhones = (array) => {
    return array
      .map((phone) => ({
        defaultPhone: false,
        phoneNumber: phone,
      }))
      .filter((phone) => phone.phoneNumber !== '');
  };

  const errorMessage = (validatedObject, message) => {
    return validatedObject.value.length > 0 ? message : undefined;
  };

  const validationUserSchema = yup.object().shape({
    firstName: yup
      .string()
      .min(2, (obj) =>
        errorMessage(obj, getTranslatedText('errors.min2', lang))
      )
      .max(50, (obj) =>
        errorMessage(obj, getTranslatedText('errors.max50', lang))
      )
      .matches(NO_SPACE, (obj) =>
        errorMessage(obj, getTranslatedText('errors.noSpace', lang))
      )
      .matches(NAME_REG_EXP, (obj) =>
        errorMessage(obj, getTranslatedText('errors.nameMatch', lang))
      )
      .default(() => firstName),
    lastName: yup
      .string()
      .min(2, (obj) =>
        errorMessage(obj, getTranslatedText('errors.min2', lang))
      )
      .max(50, (obj) =>
        errorMessage(obj, getTranslatedText('errors.max50', lang))
      )
      .matches(NO_SPACE, (obj) =>
        errorMessage(obj, getTranslatedText('errors.noSpace', lang))
      )
      .matches(NAME_REG_EXP, (obj) =>
        errorMessage(obj, getTranslatedText('errors.nameMatch', lang))
      )
      .default(() => lastName),
    phones: yup
      .array()
      .of(
        yup
          .string()
          .matches(PHONE_REG_EXP, getTranslatedText('errors.phoneMatch', lang))
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
          title: getTranslatedText('popup.errorTitle', lang),
          children: (
            <p style={{ textAlign: 'center' }}>
              {getTranslatedText('popup.notEmptyInput', lang)}
            </p>
          ),
        });
        return;
      }
      const { data } = await putUserInfo(newUserData);
      openModal({
        title: getTranslatedText('popup.serverResponse', lang),
        children: <p>{data}</p>,
      });
      setAboutLoading(false);
      dispatch(putUserToStore(newUserData));
    } catch (err) {
      setAboutLoading(false);
      if (err.response.status === 400) {
        const indexStart = err.response.data.error.indexOf(':') + 1;
        openModal({
          title: getTranslatedText('popup.serverResponse', lang),
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
              text={getTranslatedText('ownInfo.aboutMe', lang)}
            />
            <InputProfile
              label={getTranslatedText('ownInfo.firstName', lang)}
              type={'text'}
              name={'firstName'}
            />
            <InputProfile
              label={getTranslatedText('ownInfo.lastName', lang)}
              type={'text'}
              name={'lastName'}
            />
            <FieldArray name={'phones'}>
              {({ push, remove }) => {
                return (
                  <>
                    {values.phones.map((phone, index, arr) => {
                      const lastIndex = arr.length - 1;
                      const biggerThanStartIndex = arr.length > 1;
                      const maxArray = index < 10;
                      const errorField = errors.phones && errors.phones[index];
                      return (
                        <div
                          key={`phones[${index}]`}
                          style={{ position: 'relative' }}
                        >
                          <InputProfile
                            label={getTranslatedText('ownInfo.phone', lang)}
                            type="tel"
                            name={`phones[${index}]`}
                            placeholder="+38(123) 456-78-90"
                          />
                          {lastIndex === index && maxArray && (
                            <ButtonsAddRemoveChild
                              className="add-field"
                              text={getTranslatedText('button.addField', lang)}
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
                );
              }}
            </FieldArray>
            <Button
              text={getTranslatedText('button.saveChanges', lang)}
              width={'248px'}
              type={'submit'}
              whatClass={'btn-form-about-me'}
              isLoading={aboutLoading}
              disabling={Object.keys(errors).length}
              click={handleSubmit}
            />
          </>
        )}
      </Formik>

      <form>
        <div className="block-children">
          <TitleBigBlue
            whatClass="myProfile-title"
            text={getTranslatedText('ownInfo.children', lang)}
          />
          {children.map((child, idx) => {
            return (
              <div className="block-child" key={`${idx}_child`}>
                <InputProfile
                  id={idx}
                  label={getTranslatedText('ownInfo.dateOfBirth', lang)}
                  type={'date'}
                  name={'birthDate'}
                  value={child.birthDate}
                  onChange={null}
                />
                <InputGender gender={child.sex} id={idx} click={null} />
              </div>
            );
          })}
        </div>
        <Button
          disabling={false}
          text={getTranslatedText('button.saveChanges', lang)}
          width={'248px'}
          whatClass={'btn-form-children'}
        />
      </form>
    </>
  );
};
export default React.memo(MyProfile);
