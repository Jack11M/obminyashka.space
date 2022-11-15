import { createContext, useState, useMemo } from 'react';

const SearchContext = createContext();

const SearchProvider = ({ children }) => {
  const [search, setSearch] = useState('');
  const [isFetch, setIsFetch] = useState(false);

  const searchProvider = useMemo(
    () => ({
      search,
      isFetch,
      setSearch,
      setIsFetch,
    }),
    [search, isFetch]
  );

  return (
    <SearchContext.Provider value={searchProvider}>
      {children}
    </SearchContext.Provider>
  );
};

export { SearchContext, SearchProvider };
