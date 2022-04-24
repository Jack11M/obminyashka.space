import api from 'REST/Resources';
import { logOutUser, putToken, setAuthed } from './slice';

export const logoutUserThunk = () => async (dispatch) => {
  try {
    await api.fetchAuth.postAuthLogout();
    dispatch(logOutUser());
  } catch (err) {
    dispatch(logOutUser());
  }
};

export const putUserThunk = (dataFormik, checkbox) => async (dispatch) => {
  try {
    const data = await api.fetchAuth.postAuthLogin(dataFormik);
    if (checkbox) {
      localStorage.setItem('user', JSON.stringify(data));
    } else {
      sessionStorage.setItem('user', JSON.stringify(data));
    }
    dispatch(putToken(data));
    dispatch(setAuthed(true));
  } catch (err) {
    throw err;
  }
};

export const putOauthUserThunk = () => async (dispatch) => {
  try {
    const user = await api.fetchAuth.postOAuth2Success();
    if (user !== '') {
      localStorage.setItem('user', JSON.stringify(user));
      dispatch(putToken(user));
      sessionStorage.removeItem('code');
    } else {
      console.log('User not signed up via OAUTH');
      throw 'User not signed up via OAUTH';
    }
  } catch (err) {
    throw err;
  }
};
