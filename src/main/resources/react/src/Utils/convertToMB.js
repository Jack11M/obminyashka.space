export const convertToMB = (bytes) => {
  const sizes = ['Bytes', 'KB', 'MB', 'GB', 'TB'];
  if (!bytes) return '0 bytes';
  const i = parseInt(Math.floor(Math.log(bytes) / Math.log(1024)), 10);
  return {
    value: +(bytes / 1024 ** i).toFixed(2),
    valueString: `${(bytes / 1024 ** i).toFixed(2)} ${sizes[i]}`,
  };
};
