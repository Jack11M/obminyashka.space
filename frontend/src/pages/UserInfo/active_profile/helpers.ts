export const getName = (firstName: string, lastName: string) => {
  const name = [firstName, lastName];

  return name.filter((item) => item?.length > 0).join(' ');
};
