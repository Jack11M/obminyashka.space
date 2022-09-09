export const getDependWithLang = (item, lang) => {
  const locations = Object.entries(item)
    .map(([key, value]) => {
      if (key === 'id') {
        return [key, value];
      }
      if (key.endsWith(lang.toUpperCase())) {
        return [key, value];
      }
      return undefined;
    })
    .filter((value) => value);
  return Object.fromEntries(locations);
};

export const sortAlphabet = (item) => {
  function uaSort(s1, s2) {
    return s1.localeCompare(s2);
  }
  return item.sort(uaSort);
};
