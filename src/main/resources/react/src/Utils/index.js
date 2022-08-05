export {
  addChild,
  addPhone,
  getArray,
  deleteItem,
  getMinDate,
  putChildren,
  fillUserInfo,
  genderChange,
  dateValidation,
  getCurrentDate,
  deleteLastPhone,
  deleteReceivedChildren,
  changePhoneInputOrChildren,
} from './profileUtils';
export {
  setStorageUser,
  getStorageUser,
  getStorageLang,
  removeTokenFromStorage,
} from './storage';
export {
  errorHandling,
  translateErrors,
  permissionToSendProfile,
  permissionToSendChildren,
} from './error';

export { authError, isErrorArray, translateErrorsAuth } from './errorAuth';
