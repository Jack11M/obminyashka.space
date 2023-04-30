/* eslint-disable */
// @ts-nocheck
// TODO: fix typescript
import { showMessage } from 'obminyashka-components';

import api from 'src/REST/Resources';
import { getErrorMessage } from 'src/Utils/error';

import { putChildrenToStore, putProfile, setAvatar } from './slice';

export const getUserThunk = () => async (dispatch) => {
  try {
    const data = await api.profile.getUserInfo();
    dispatch(putProfile(data));
  } catch (e: any) {
    if (e?.response?.status !== 401) {
      showMessage.error(getErrorMessage(e));
    }
  }
};

export const putChildrenThunk = (children) => async (dispatch) => {
  try {
    const data = await api.profile.putUserChildren(children);
    dispatch(putChildrenToStore(data));
  } catch (e) {
    showMessage.error(getErrorMessage(e));
  }
};

export const postAvatarThunk = (avatar) => async (dispatch) => {
  try {
    const received = await api.profile.postAvatar(avatar);
    const convertToBase64 = `data:image/jpeg;base64,${received.avatarImage}`;
    dispatch(setAvatar(convertToBase64));
    return Promise.resolve(convertToBase64);
  } catch (e) {
    return Promise.reject(e);
  }
};

export const deleteAvatarThunk = () => async (dispatch) => {
  try {
    await api.profile.deleteAvatar();
    dispatch(setAvatar(''));
    return Promise.resolve();
  } catch (e) {
    return Promise.reject(e);
  }
};
