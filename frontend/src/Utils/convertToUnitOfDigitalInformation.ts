export const MAX_SIZE_PHOTO = 10;
const extension = ['jpg', 'jpeg', 'png', 'gif'];
const ONE_TERABYTE = 1099511627776

export const convertToUnitOfDigitalInformation = (bytes: number) => {
  const sizes = ['Bytes', 'KB', 'MB', 'GB', 'TB'];
  if (!bytes || bytes < 0 || bytes >= ONE_TERABYTE) return '0 bytes';
  const i = Math.floor(Math.log(bytes) / Math.log(1024));
  const numberWithTwoDigits = (bytes / 1024 ** i).toFixed(2);
  return {
    value: +numberWithTwoDigits,
    valueString: `${numberWithTwoDigits} ${sizes[i]}`,
  };
};

export const options = {
  maxSizeMB: MAX_SIZE_PHOTO,
  maxWidthOrHeight: 1920,
  useWebWorker: true,
  maxIteration: 10,
};

export const isImageExtensionSupported = (type: string) => {
  return extension.includes(type.replace('image/', ''));
};
