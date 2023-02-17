import { useState, useContext } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';
import { Button } from '@wolshebnik/obminyashka-components';

import * as Icon from 'assets/icons';
import { route } from 'routes/routeConstants';
import { SearchContext } from 'components/common';
import { getTranslatedText } from 'components/local/localization';
import { ReactComponent as SearchSvg } from 'assets/icons/search.svg';

import * as Styles from './styles';
import NavCategory from '../nav_category/index';

const NavMain = () => {
  const navigate = useNavigate();
  const { pathname } = useLocation();
  const { search, setSearch, setIsFetch } = useContext(SearchContext);
  const [open, setOpen] = useState(false);

  const change = (e) => {
    setIsFetch(false);
    setSearch(e.target.value);
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
            <Styles.InputSearch
              type="text"
              value={search}
              onChange={change}
              onKeyPress={keyEnter}
              placeholder={`${getTranslatedText('header.iSearch')} ...`}
            />

            {search && (
              <Styles.IconBlock
                onClick={() => {
                  setSearch('');
                  setIsFetch(true);
                }}
              >
                <Icon.CloseSvg />
              </Styles.IconBlock>
            )}

            <Styles.LabelLink onClick={move} htmlFor="search">
              <SearchSvg />
            </Styles.LabelLink>
          </Styles.WrapSearch>

          <Button
            width={295}
            colorType="green"
            icon={<Icon.Plus />}
            style={{ height: 50 }}
            onClick={() => navigate(route.addAdv)}
            text={getTranslatedText('button.addAdv')}
          />
        </Styles.WrapMain>
      </Styles.Wrapper>
    </Styles.DivWrap>
  );
};

export default NavMain;
