import { postAuthLogin, postAuthLogout } from 'REST/Resources';
import { logOutUser, putToken } from './slice';

export const logoutUserThunk = () => async (dispatch) => {
  try {
    await postAuthLogout();
    dispatch(logOutUser());
  } catch (err) {}
};

export const putUserThunk = (dataFormik, checkbox) => async (dispatch) => {
  try {
    const data = await postAuthLogin(dataFormik);
    if (checkbox) {
      localStorage.setItem('user', JSON.stringify(data));
    } else {
      sessionStorage.setItem('user', JSON.stringify(data));
    }
    dispatch(putToken(data));
  } catch (err) {}
};
