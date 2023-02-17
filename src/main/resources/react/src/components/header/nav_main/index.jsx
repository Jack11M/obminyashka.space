import { useState, useContext } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';
import { Input } from '@wolshebnik/obminyashka-components';

import { route } from 'routes/routeConstants';
import { ButtonAdv, SearchContext } from 'components/common';
import { getTranslatedText } from 'components/local/localization';

import * as Styles from './styles';
import NavCategory from '../nav_category/index';

const NavMain = () => {
  const navigate = useNavigate();
  const { pathname } = useLocation();
  const { search, setSearch, setIsFetch } = useContext(SearchContext);
  const [open, setOpen] = useState(false);

  const change = (e) => {
    setIsFetch(false);
    setSearch(typeof e === 'string' ? e : e.target.value);
  };

  const move = () => {
    setIsFetch(true);

    if (pathname?.replace('/', '') !== route.SearchResults) {
      navigate(route.SearchResults);
    }
  };

  const keyEnter = (event) => {
    if (!search) {
      if (event.key === 'Enter') event.preventDefault();
      return;
    }

    if (event.key === 'Enter') {
      event.preventDefault();
      move();
    }
  };

  return (
    <Styles.DivWrap>
      <Styles.Wrapper>
        <Styles.WrapMain>
          <Styles.LogoLink to={route.home} />

          <Styles.WrapCategories open={open} onClick={() => setOpen(!open)}>
            <Styles.WrapCategoriesTop>
              {getTranslatedText('header.categories')}
            </Styles.WrapCategoriesTop>

            <Styles.WrapCategoriesBottom>
              {getTranslatedText('header.categories')}
            </Styles.WrapCategoriesBottom>

            {open && <NavCategory />}
          </Styles.WrapCategories>

          <Styles.WrapSearch>
            <Input
              name="search"
              type="search"
              value={search}
              onClick={() => move()}
              onKeyDown={(e) => keyEnter(e)}
              onChange={(e) => change(e)}
              placeholder={`${getTranslatedText('header.iSearch')} ...`}
            />
          </Styles.WrapSearch>

          <ButtonAdv type="link" />
        </Styles.WrapMain>
      </Styles.Wrapper>
    </Styles.DivWrap>
  );
};

export default NavMain;
