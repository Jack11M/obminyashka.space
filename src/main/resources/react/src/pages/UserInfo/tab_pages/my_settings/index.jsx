import { memo, useContext, useState } from 'react';
import { Formik } from 'formik';
import { useDispatch, useSelector } from 'react-redux';

import api from 'REST/Resources';
import { showMessage } from 'hooks';
import { route } from 'routes/routeConstants';
import { putEmail, getAuthProfile } from 'store/auth/slice';
import { getTranslatedText } from 'components/local/localization';
import { TitleBigBlue, ModalContext, Button } from 'components/common';

import InputProfile from '../../components/inputProfile';
import { validationEmailSchema, validationPasswordSchema } from './config';

import * as Styles from './styles';

const MySettings = () => {
  const dispatch = useDispatch();
  const { openModal } = useContext(ModalContext);

  const [isFetchPass, setIsFetchPass] = useState(false);
  const [isFetchEmail, setIsFetchEmail] = useState(false);

  const { email: currentEmail } = useSelector(getAuthProfile);

  const validationPassword = validationPasswordSchema;

  const validationEmail = validationEmailSchema(currentEmail);

  const initialEmailValues = validationEmail.cast({});
  const initialPasswordValues = validationPassword.cast({});

  const handlePassword = async (values, onSubmitProps) => {
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
  };

  const handleEmail = async (values, onSubmitProps) => {
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
        showMessage(e.response.data.error);
        onSubmitProps.setErrors({ newEmail: e.response.data.error });
      }
    }
  };

  return (
    <>
      <TitleBigBlue
        style={{ margin: '65px 0 40px' }}
        text={getTranslatedText('settings.changePassword')}
      />

      <Formik
        onSubmit={handlePassword}
        initialValues={initialPasswordValues}
        validationSchema={validationPassword}
      >
        {({ errors, isValid, handleSubmit, dirty }) => (
          <>
            <Styles.InputContainer>
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
            </Styles.InputContainer>

            <Styles.ButtonContainer>
              <Button
                type="submit"
                width="248px"
                height="49px"
                isLoading={isFetchPass}
                disabling={!dirty && !isValid}
                text={getTranslatedText('button.save')}
                click={!errors.oldPassword ? handleSubmit : null}
              />
            </Styles.ButtonContainer>
          </>
        )}
      </Formik>

      <TitleBigBlue
        style={{ margin: '65px 0 40px' }}
        text={getTranslatedText('settings.changeEmail')}
      />

      <Formik
        onSubmit={handleEmail}
        initialValues={initialEmailValues}
        validationSchema={validationEmail}
      >
        {({ errors, isValid, handleSubmit, dirty }) => (
          <>
            <Styles.InputContainer>
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
            </Styles.InputContainer>

            <Styles.ButtonContainer>
              <Button
                type="submit"
                width="363px"
                height="49px"
                isLoading={isFetchEmail}
                style={{ margin: '50px 0' }}
                disabling={!isValid && !dirty}
                click={!errors.newEmail ? handleSubmit : null}
                text={getTranslatedText('button.saveEmail')}
              />
            </Styles.ButtonContainer>
          </>
        )}
      </Formik>

      <TitleBigBlue
        style={{ margin: '65px 0 40px' }}
        text={getTranslatedText('settings.remove')}
      />

      <Styles.WarningText>
        {getTranslatedText('settings.describe')}{' '}
        <Styles.StylizedLink to={`${route.userInfo}${route.myProfile}`}>
          &nbsp;{getTranslatedText('settings.profile')}
        </Styles.StylizedLink>
      </Styles.WarningText>

      <Styles.ButtonContainer>
        <Button
          width="248px"
          style={{ marginBottom: '65px' }}
          text={getTranslatedText('button.remove')}
        />
      </Styles.ButtonContainer>
    </>
  );
};

export default memo(MySettings);
