/* eslint-disable */
// @ts-nocheck
// TODO: fix typescript
import { createContext, useState, useMemo, useEffect } from 'react';
import { useSearchParams } from 'react-router-dom';

interface IProvider {
  search: string;
  isFetch: boolean;
  setSearch: React.Dispatch<React.SetStateAction<string>>;
  setIsFetch: React.Dispatch<React.SetStateAction<boolean>>;
}

const initialContext = {
  search: '',
  isFetch: false,
  setSearch: () => {},
  setIsFetch: () => {},
};

const SearchContext = createContext<IProvider>(initialContext);

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

  return <SearchContext.Provider value={searchProvider}>{children}</SearchContext.Provider>;
};

export { SearchContext, SearchProvider };
