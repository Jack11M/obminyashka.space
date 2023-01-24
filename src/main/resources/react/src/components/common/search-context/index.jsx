import { createContext, useState, useMemo, useEffect } from 'react';
import { useSearchParams } from 'react-router-dom';

const SearchContext = createContext();

const SearchProvider = ({ children }) => {
  const [searchParams] = useSearchParams();
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

  useEffect(() => {
    const searchResults = searchParams.get('search') || '';
    setSearch(searchResults);
  }, []);

  return (
    <SearchContext.Provider value={searchProvider}>
      {children}
    </SearchContext.Provider>
  );
};

export { SearchContext, SearchProvider };
