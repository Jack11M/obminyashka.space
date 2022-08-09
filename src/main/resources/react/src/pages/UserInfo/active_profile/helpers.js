export const getName = (firstName, lastName) => {
  const name = [firstName, lastName];

  return name.filter((item) => item.length > 0).join(' ');
};
