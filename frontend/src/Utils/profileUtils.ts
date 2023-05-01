/* eslint-disable */
// @ts-nocheck
// TODO: fix typescript
const PHONE_MAX = 5;
const CHILDREN_MAX = 10;
const MAX_AGE = 17;
const date = new Date();

export const getCurrentDate = () => date.toISOString().substr(0, 10);

export const getMinDate = () => {
  const year = getCurrentDate().slice(0, 4) - MAX_AGE;
  const rest = getCurrentDate().slice(4);
  return `${year}${rest}`;
};

export const fillUserInfo = (data) => {
  const { phones, children, ...rest } = data;
  if (!phones.length && !children.length) {
    return rest;
  }
  if (!phones.length) {
    return { ...children, ...rest };
  }
  return {};
};

export const changePhoneInputOrChildren = (state, value, id, property) =>
  state.map((item, idx) => (idx === id ? { ...item, [property]: value } : item));

export const addPhone = (data, errors) => {
  if (
    !data.length ||
    data.length >= PHONE_MAX ||
    data[data.length - 1].phoneNumber === '' ||
    errors.length
  ) {
    return data;
  }
  return [...data, { defaultPhone: false, id: 0, phoneNumber: '' }];
};

export const putChildren = (state, child) => {
  const arrayChildren = [...state];
  arrayChildren[arrayChildren.length - 1] = child;
  return arrayChildren;
};

export const genderChange = (state, sex, id) =>
  state.map((child, idx) => (idx === id ? { ...child, sex } : child));

export const deleteLastPhone = (data) => {
  const newData = [...data];
  newData.pop();
  return newData;
};

export const addChild = (state, errors) => {
  if (
    !state.length ||
    state.length >= CHILDREN_MAX ||
    state[state.length - 1].birthDate === '' ||
    errors.length
  ) {
    return state;
  }
  const newChild = {
    id: 0,
    birthDate: '',
    sex: 'UNSELECTED',
  };
  return [...state, newChild];
};

export const deleteItem = (state, id) => state.filter((_item, idx) => idx !== id);
export const deleteReceivedChildren = (state, id) => state.filter((item) => item.id !== id);

export const dateValidation = (dateValue) => {
  const chooseDate = Date.parse(dateValue);
  const currentDate = Date.parse(getCurrentDate());
  const lastDate = Date.parse(getMinDate());
  return chooseDate >= lastDate && chooseDate <= currentDate;
};

export const getArray = (state, mock) => (!state.length ? mock : state);
