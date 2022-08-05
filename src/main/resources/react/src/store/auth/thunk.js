import api from 'REST/Resources';
import { showMessage } from 'hooks';

import { logOutUser, putToken, setAuthed } from './slice';

export const logoutUserThunk = () => async (dispatch) => {
  try {
    await api.auth.postAuthLogout();
    dispatch(logOutUser());
  } catch (err) {
    dispatch(logOutUser());
  }
};

export const putUserThunk = (dataFormik, checkbox) => async (dispatch) => {
  try {
    const data = await api.auth.postAuthLogin(dataFormik);
    if (checkbox) {
      localStorage.setItem('user', JSON.stringify(data));
    } else {
      sessionStorage.setItem('user', JSON.stringify(data));
    }
    dispatch(putToken(data));
    dispatch(setAuthed(true));
    return Promise.resolve();
  } catch (err) {
    return Promise.reject(err);
  }
};

export const putOauthUserThunk = () => async (dispatch) => {
  try {
    const user = await api.auth.postOAuth2Success();
    if (user !== '') {
      localStorage.setItem('user', JSON.stringify(user));
      dispatch(putToken(user));
      sessionStorage.removeItem('code');
    } else {
      showMessage('User not signed up via OAUTH');
    }
  } catch (err) {
    showMessage(err);
  }
};
