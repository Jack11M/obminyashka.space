import { useState } from 'react';

import { route } from 'routes/routeConstants';
import { ButtonAdv } from 'components/common';
import { getTranslatedText } from 'components/local/localization';
import { ReactComponent as SearchSvg } from 'assets/icons/search.svg';

import * as Styles from './styles';
import NavCategory from '../nav_category/index';

const NavMain = () => {
  const [open, setOpen] = useState(false);

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
              placeholder={`${getTranslatedText('header.iSearch')} ...`}
            />
            <Styles.LabelLink to={route.SearchResultsPage} htmlFor="search">
              <SearchSvg />
            </Styles.LabelLink>
          </Styles.WrapSearch>

          <ButtonAdv type="link" />
        </Styles.WrapMain>
      </Styles.Wrapper>
    </Styles.DivWrap>
  );
};

export default NavMain;
