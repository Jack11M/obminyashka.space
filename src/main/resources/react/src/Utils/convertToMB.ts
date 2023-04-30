/* eslint-disable */
// @ts-nocheck
// TODO: fix typescript
import { MAX_SIZE_PHOTO } from './constants';

const extension = ['jpg', 'jpeg', 'png', 'gif'];

export const convertToMB = (bytes) => {
  const sizes = ['Bytes', 'KB', 'MB', 'GB', 'TB'];
  if (!bytes) return '0 bytes';
  const i = parseInt(Math.floor(Math.log(bytes) / Math.log(1024)), 10);
  return {
    value: +(bytes / 1024 ** i).toFixed(2),
    valueString: `${(bytes / 1024 ** i).toFixed(2)} ${sizes[i]}`,
  };
};

export const options = {
  maxSizeMB: MAX_SIZE_PHOTO,
  maxWidthOrHeight: 1920,
  useWebWorker: true,
  maxIteration: 10,
};

export const isRightExtension = (type) => {
  return !extension.includes(type.replace('image/', ''));
};
