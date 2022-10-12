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

export { city, district, area } from './getLocationProperties';

export { useDelay } from './delay';
export { convertToMB } from './convertToMB';

export { authError, isErrorArray, translateErrorsAuth } from './errorAuth';
