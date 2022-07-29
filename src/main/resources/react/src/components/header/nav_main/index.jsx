import { useSelector } from 'react-redux';

import { getLang } from 'store/auth/slice';
import { route } from 'routes/routeConstants';
import { ButtonAdv } from 'components/common/buttons';
import { getTranslatedText } from 'components/local/localization';
import { ReactComponent as SearchSvg } from 'assets/icons/search.svg';

import * as Styles from './styles';
import NavCategory from '../nav_category';

const NavMain = () => {
  const lang = useSelector(getLang);
  return (
    <Styles.DivWrap>
      <Styles.Wrapper>
        <Styles.WrapMain>
          <Styles.LogoLink to={route.home} />

          <Styles.WrapCategories>
            <Styles.WrapCategoriesTop>
              {getTranslatedText('header.categories', lang)}
            </Styles.WrapCategoriesTop>
            <Styles.WrapCategoriesBottom>
              {getTranslatedText('header.categories', lang)}
            </Styles.WrapCategoriesBottom>
            <NavCategory />
          </Styles.WrapCategories>

          <Styles.WrapSearch>
            <Styles.InputSearch
              type="text"
              placeholder={`${getTranslatedText('header.iSearch', lang)} ...`}
            />

            <Styles.Label htmlFor="search">
              <SearchSvg />
            </Styles.Label>
          </Styles.WrapSearch>

          <ButtonAdv type="link" />
        </Styles.WrapMain>
      </Styles.Wrapper>
    </Styles.DivWrap>
  );
};

export default NavMain;
