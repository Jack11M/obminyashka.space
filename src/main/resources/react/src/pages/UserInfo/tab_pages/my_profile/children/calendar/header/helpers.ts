export const range = (start, end) => {
  return new Array(end - start).fill().map((_, i) => i + start);
};
