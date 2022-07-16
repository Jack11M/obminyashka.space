export const range = (start, end) => {
  return new Array(end - start).fill().map((d, i) => i + start);
};
