/* eslint-disable */
// @ts-nocheck
// TODO: fix typescript
export const range = (start: number, end: number) => {
  return new Array(end - start).fill().map((_, i) => i + start);
};
