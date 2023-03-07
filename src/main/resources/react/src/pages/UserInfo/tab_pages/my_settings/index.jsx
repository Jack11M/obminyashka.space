import { memo, useContext, useState } from 'react';
import { Form, Formik } from 'formik';
import { useDispatch, useSelector } from 'react-redux';
import { Button, Title, showMessage, InputField } from 'obminyashka-components';

import api from 'REST/Resources';
import { route } from 'routes/routeConstants';
import { setProfileEmail } from 'store/profile/slice';
import { getAuthProfile, putEmail } from 'store/auth/slice';
import { ModalContext } from 'components/common';
import { getTranslatedText } from 'components/local/localization';

import InputProfile from '../../components/inputProfile';

import {
  initialValuesDelete,
  validationEmailSchema,
  validationDeleteSchema,
  validationPasswordSchema,
} from './config';

import * as Styles from './styles';

const MySettings = () => {
  const dispatch = useDispatch();
  const { openModal } = useContext(ModalContext);
  const { email: currentEmail } = useSelector(getAuthProfile);

  const [loading, setLoading] = useState(false);
  const [isFetchPass, setIsFetchPass] = useState(false);
  const [isFetchEmail, setIsFetchEmail] = useState(false);

  const validationPassword = validationPasswordSchema;
  const validationEmail = validationEmailSchema(currentEmail);

  const initialEmailValues = validationEmail.cast({});
  const initialPasswordValues = validationPassword.cast({});

  const handlePassword = async (values, onSubmitProps) => {
    setIsFetchPass(true);
    try {
      const data = await api.profile.putPasswordFetch(values);
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
      const data = await api.profile.putEmailFetch({
        newEmail,
        newEmailConfirmation,
      });
      openModal({
        title: getTranslatedText('popup.serverResponse'),
        children: <p>{data}</p>,
      });
      dispatch(putEmail(newEmail));
      dispatch(setProfileEmail(newEmail));
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
        showMessage.error(e.response.data.error);
        onSubmitProps.setErrors({ newEmail: e.response.data.error });
      }
    }
  };

  const handleDelete = async (values) => {
    const { confirmPassword, password } = values;
    try {
      setLoading(true);
      const data = await api.profile.deleteUserAccount({
        confirmPassword,
        password,
      });
      openModal({
        title: (
          <Styles.ModalTitle>
            {getTranslatedText('settings.removed')}
          </Styles.ModalTitle>
        ),
        children: <p>{data}</p>,
      });
    } catch (e) {
      showMessage.error(e.response.data.error);
    } finally {
      setLoading(false);
    }
  };

  const deleteAccountModal = () => {
    openModal({
      title: (
        <Styles.ModalTitle>
          {getTranslatedText('settings.remove')}
        </Styles.ModalTitle>
      ),
      children: (
        <div>
          <p>{getTranslatedText('settings.removeConfirm')}</p>

          <Formik
            onSubmit={handleDelete}
            initialValues={initialValuesDelete}
            validationSchema={validationDeleteSchema}
          >
            {() => (
              <Form>
                <Styles.InputWrapper>
                  <InputField
                    inputGap="3px"
                    type="password"
                    name="password"
                    label={getTranslatedText('auth.regPassword')}
                  />

                  <InputField
                    inputGap="3px"
                    type="password"
                    name="confirmPassword"
                    label={getTranslatedText('auth.regConfirm')}
                  />
                </Styles.InputWrapper>

                <Button
                  width={248}
                  type="submit"
                  isLoading={loading}
                  text={getTranslatedText('button.remove')}
                />
              </Form>
            )}
          </Formik>
        </div>
      ),
    });
  };

  return (
    <>
      <Title
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
              <InputField
                type="password"
                name="oldPassword"
                labelColor="black"
                inputMaxWidth="588px"
                inputFlexDirection="row"
                wrapperInputErrorWidth="415px"
                inputJustifyContent="space-between"
                label={getTranslatedText('settings.currentPassword')}
              />

              <InputField
                type="password"
                name="newPassword"
                labelColor="black"
                inputMaxWidth="588px"
                inputFlexDirection="row"
                wrapperInputErrorWidth="415px"
                inputJustifyContent="space-between"
                label={getTranslatedText('settings.newPassword')}
              />

              <InputField
                type="password"
                labelColor="black"
                inputMaxWidth="588px"
                inputFlexDirection="row"
                name="confirmNewPassword"
                wrapperInputErrorWidth="415px"
                inputJustifyContent="space-between"
                label={getTranslatedText('settings.confirmPassword')}
              />
            </Styles.InputContainer>

            <Styles.ButtonContainer>
              <Button
                width={248}
                type="submit"
                style={{ height: 49 }}
                isLoading={isFetchPass}
                disabling={!isValid && !dirty}
                text={getTranslatedText('button.saveChanges')}
                onClick={!errors.oldPassword ? handleSubmit : null}
              />
            </Styles.ButtonContainer>
          </>
        )}
      </Formik>

      <Title
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
                type="text"
                name="oldEmail"
                value={currentEmail}
                label={getTranslatedText('settings.oldEmail')}
              />

              <InputProfile
                type="text"
                name="newEmail"
                label={getTranslatedText('settings.newEmail')}
              />

              <InputProfile
                type="text"
                name="newEmailConfirmation"
                label={getTranslatedText('settings.confirmEmail')}
              />
            </Styles.InputContainer>

            <Styles.ButtonContainer>
              <Button
                width={248}
                type="submit"
                isLoading={isFetchEmail}
                disabling={!isValid && !dirty}
                style={{ margin: '50px 0', height: 49 }}
                text={getTranslatedText('button.saveChanges')}
                onClick={!errors.newEmail ? handleSubmit : null}
              />
            </Styles.ButtonContainer>
          </>
        )}
      </Formik>

      <Title
        style={{ margin: '65px 0 40px' }}
        text={getTranslatedText('settings.remove')}
      />

      <Styles.WarningText>
        {getTranslatedText('settings.describe')}
        <Styles.StylizedLink to={`/${route.userInfo}/${route.myProfile}`}>
          &nbsp;{getTranslatedText('settings.profile')}
        </Styles.StylizedLink>
      </Styles.WarningText>

      <Styles.ButtonContainer>
        <Button
          width={248}
          onClick={deleteAccountModal}
          style={{ marginBottom: '65px' }}
          text={getTranslatedText('button.remove')}
        />
      </Styles.ButtonContainer>
    </>
  );
};

export default memo(MySettings);
