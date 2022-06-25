import api from 'REST/Resources';
import { showMessage } from 'hooks';
import { getErrorMessage } from 'Utils/error';

import { putProfile } from './slice';

export const getUserThunk = () => async (dispatch) => {
  try {
    const data = await await api.fetchProfile.getUserInfo();
    dispatch(putProfile(data));
  } catch (e) {
    showMessage(getErrorMessage(e));
  }
};
