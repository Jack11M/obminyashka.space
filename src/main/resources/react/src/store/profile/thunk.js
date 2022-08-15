import api from 'REST/Resources';
import { showMessage } from 'hooks';
import { getErrorMessage } from 'Utils/error';

import { putChildrenToStore, putProfile, setAvatar } from './slice';

export const getUserThunk = () => async (dispatch) => {
  try {
    const data = await api.profile.getUserInfo();
    dispatch(putProfile(data));
  } catch (e) {
    showMessage(getErrorMessage(e));
  }
};

export const putChildrenThunk = (children) => async (dispatch) => {
  try {
    const data = await api.profile.putUserChildren(children);
    dispatch(putChildrenToStore(data));
  } catch (e) {
    showMessage(getErrorMessage(e));
  }
};

export const postAvatarThunk = (avatar) => async (dispatch) => {
  try {
    const data = await api.profile.postAvatar(avatar);
    dispatch(setAvatar(data));
    return Promise.resolve();
  } catch (e) {
    return Promise.reject(e);
  }
};
