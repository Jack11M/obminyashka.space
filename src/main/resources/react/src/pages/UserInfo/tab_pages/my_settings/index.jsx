import { memo, useContext, useState } from 'react';
import * as yup from 'yup';
import { Formik } from 'formik';
import { Link } from 'react-router-dom';
import { useDispatch, useSelector } from 'react-redux';

import api from 'REST/Resources';
import { route } from 'routes/routeConstants';
import { putEmail, getAuthProfile } from 'store/auth/slice';
import { getTranslatedText } from 'components/local/localization';
import { TitleBigBlue, ModalContext, Button } from 'components/common';
import { EMAIL_REG_EXP, PASSWORD_ALT_CODE_EXP, PASSWORD_REG_EXP } from 'config';

import InputProfile from '../../components/inputProfile';

import './mySettings.scss';

const MySettings = () => {
  const { openModal } = useContext(ModalContext);
  const dispatch = useDispatch();

  const [isFetchPass, setIsFetchPass] = useState(false);
  const [isFetchEmail, setIsFetchEmail] = useState(false);

  const { email: currentEmail } = useSelector(getAuthProfile);

  const validationPasswordSchema = yup.object().shape({
    oldPassword: yup
      .string()
      .min(8, getTranslatedText('errors.min8'))
      .max(30, getTranslatedText('errors.max30'))
      .matches(PASSWORD_REG_EXP, getTranslatedText('errors.passwordMatch'))
      .matches(PASSWORD_ALT_CODE_EXP, getTranslatedText('errors.altCodeMatch'))
      .required(getTranslatedText('errors.requireField'))
      .default(() => ''),
    newPassword: yup
      .string()
      .min(8, getTranslatedText('errors.min8'))
      .max(30, getTranslatedText('errors.max30'))
      .matches(PASSWORD_REG_EXP, getTranslatedText('errors.passwordMatch'))
      .matches(PASSWORD_ALT_CODE_EXP, getTranslatedText('errors.altCodeMatch'))
      .notOneOf(
        [yup.ref('oldPassword'), false],
        getTranslatedText('errors.passwordIdentical')
      )
      .required(getTranslatedText('errors.requireField'))
      .default(() => ''),
    confirmNewPassword: yup
      .string()
      .oneOf(
        [yup.ref('newPassword')],
        getTranslatedText('errors.passwordMismatch')
      )
      .required(getTranslatedText('errors.requireField'))
      .default(() => ''),
  });

  const validationEmailSchema = yup.object().shape({
    newEmail: yup
      .string()
      .notOneOf([currentEmail], getTranslatedText('errors.emailIdentical'))
      .matches(EMAIL_REG_EXP, getTranslatedText('errors.max129'))
      .email(getTranslatedText('errors.invalidEmailFormat'))
      .required(getTranslatedText('errors.requireField'))
      .default(() => ''),
    newEmailConfirmation: yup
      .string()
      .oneOf(
        [yup.ref('newEmail')],
        getTranslatedText('errors.emailNotIdentical')
      )
      .email(getTranslatedText('errors.invalidEmailFormat'))
      .required(getTranslatedText('errors.requireField'))
      .default(() => ''),
  });

  const initialPasswordValues = validationPasswordSchema.cast({});
  const initialEmailValues = validationEmailSchema.cast({});

  return (
    <>
      <TitleBigBlue
        whatClass="myProfile-title"
        text={getTranslatedText('settings.changePassword')}
      />
      <Formik
        initialValues={initialPasswordValues}
        validateOnBlur
        validationSchema={validationPasswordSchema}
        onSubmit={async (values, onSubmitProps) => {
          setIsFetchPass(true);
          try {
            const { data } = await api.fetchProfile.putPasswordFetch(values);
            openModal({
              title: getTranslatedText('popup.serverResponse'),
              children: <p>{data}</p>,
            });
            onSubmitProps.resetForm();
            setIsFetchPass(false);
          } catch (e) {
            setIsFetchPass(false);
            if (e.response.status === 400) {
              onSubmitProps.setErrors({ oldPassword: e.response.data.error });
            }
          }
        }}
      >
        {({ errors, isValid, handleSubmit, dirty }) => (
          <>
            <div className="info-one">
              <InputProfile
                type="password"
                name="oldPassword"
                label={getTranslatedText('settings.currentPassword')}
              />
              <InputProfile
                type="password"
                name="newPassword"
                label={getTranslatedText('settings.newPassword')}
              />
              <InputProfile
                type="password"
                name="confirmNewPassword"
                label={getTranslatedText('settings.confirmPassword')}
              />
            </div>
            <Button
              type="submit"
              width="248px"
              height="49px"
              whatClass="btn-profile"
              isLoading={isFetchPass}
              disabling={!dirty && !isValid}
              text={getTranslatedText('button.save')}
              click={!errors.oldPassword ? handleSubmit : null}
            />
          </>
        )}
      </Formik>

      <TitleBigBlue
        whatClass="myProfile-title"
        text={getTranslatedText('settings.changeEmail')}
      />
      <Formik
        validateOnBlur
        initialValues={initialEmailValues}
        validationSchema={validationEmailSchema}
        onSubmit={async (values, onSubmitProps) => {
          const { newEmail, newEmailConfirmation } = values;
          setIsFetchEmail(true);
          try {
            const { data } = await api.fetchProfile.putEmailFetch({
              newEmail,
              newEmailConfirmation,
            });
            openModal({
              title: getTranslatedText('popup.serverResponse'),
              children: <p>{data}</p>,
            });
            dispatch(putEmail(newEmail));
            onSubmitProps.resetForm();
            setIsFetchEmail(false);
          } catch (e) {
            setIsFetchEmail(false);
            if (e.response.status === 409) {
              onSubmitProps.setErrors({
                newEmailConfirmation: e.response.data.error,
              });
            }
            if (e.response.status === 400) {
              console.log(e.response.data.error);
              onSubmitProps.setErrors({ newEmail: e.response.data.error });
            }
          }
        }}
      >
        {({ errors, isValid, handleSubmit, dirty }) => (
          <>
            <div className="info-one">
              <InputProfile
                readOnly
                type="email"
                name="oldEmail"
                value={currentEmail}
                label={getTranslatedText('settings.oldEmail')}
              />
              <InputProfile
                type="email"
                name="newEmail"
                label={getTranslatedText('settings.newEmail')}
              />
              <InputProfile
                type="email"
                name="newEmailConfirmation"
                label={getTranslatedText('settings.confirmEmail')}
              />
            </div>
            <Button
              type="submit"
              width="300px"
              height="49px"
              isLoading={isFetchEmail}
              disabling={!isValid && !dirty}
              whatClass="btn-profile e-mail-button"
              click={!errors.newEmail ? handleSubmit : null}
              text={getTranslatedText('button.saveEmail')}
            />
          </>
        )}
      </Formik>
      <TitleBigBlue
        whatClass="myProfile-title"
        text={getTranslatedText('settings.remove')}
      />
      <p className="delete-text">
        {getTranslatedText('settings.describe')}{' '}
        <Link to={`${route.userInfo}${route.myProfile}`}>
          &nbsp;{getTranslatedText('settings.profile')}
        </Link>
      </p>

      <div className="btn-wrapper">
        <Button
          width="248px"
          whatClass="btn-profile"
          text={getTranslatedText('button.remove')}
        />
      </div>
    </>
  );
};

export default memo(MySettings);
