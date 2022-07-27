import { route } from 'routes/routeConstants';
import {
  toys,
  books,
  other,
  shoes,
  clothes,
  furniture,
  kidsUpToYear,
  transportForChildren,
} from 'assets/img/all_images_export/navItems';
import { getTranslatedText } from 'components/local/localization';

import * as Styles from './styles';

const NavCategory = () => {
  return (
    <Styles.List>
      <Styles.Wrapper>
        <Styles.NavbarLinkContainer>
          <Styles.NavbarLink to={route.home}>
            <Styles.Img src={clothes} alt="clothes" />
            <Styles.Span>{getTranslatedText('header.clothes')}</Styles.Span>
          </Styles.NavbarLink>
        </Styles.NavbarLinkContainer>

        <Styles.NavbarLinkContainer>
          <Styles.NavbarLink to={route.home}>
            <Styles.Img src={shoes} alt="shoes" />
            <Styles.Span>{getTranslatedText('header.shoes')}</Styles.Span>
          </Styles.NavbarLink>
        </Styles.NavbarLinkContainer>

        <Styles.NavbarLinkContainer>
          <Styles.NavbarLink to={route.home}>
            <Styles.Img src={toys} alt="toys" />
            <Styles.Span>{getTranslatedText('header.toys')}</Styles.Span>
          </Styles.NavbarLink>
        </Styles.NavbarLinkContainer>

        <Styles.NavbarLinkContainer>
          <Styles.NavbarLink to={route.home}>
            <Styles.Img src={transportForChildren} alt="transportForChildren" />
            <Styles.Span>{getTranslatedText('header.vehicles')}</Styles.Span>
          </Styles.NavbarLink>
        </Styles.NavbarLinkContainer>

        <Styles.NavbarLinkContainer>
          <Styles.NavbarLink to={route.home}>
            <Styles.Img src={furniture} alt="furniture" />
            <Styles.Span>{getTranslatedText('header.furniture')}</Styles.Span>
          </Styles.NavbarLink>
        </Styles.NavbarLinkContainer>

        <Styles.NavbarLinkContainer>
          <Styles.NavbarLink to={route.home}>
            <Styles.Img src={kidsUpToYear} alt="kidsUpToYear" />
            <Styles.Span>{getTranslatedText('header.babies')}</Styles.Span>
          </Styles.NavbarLink>
        </Styles.NavbarLinkContainer>

        <Styles.NavbarLinkContainer>
          <Styles.NavbarLink to={route.home}>
            <Styles.Img src={books} alt="books" />
            <Styles.Span>{getTranslatedText('header.books')}</Styles.Span>
          </Styles.NavbarLink>
        </Styles.NavbarLinkContainer>

        <Styles.NavbarLinkContainer>
          <Styles.NavbarLink to={route.home}>
            <Styles.Img src={other} alt="other" />
            <Styles.Span>{getTranslatedText('header.another')}</Styles.Span>
          </Styles.NavbarLink>
        </Styles.NavbarLinkContainer>
      </Styles.Wrapper>
    </Styles.List>
  );
};

export default NavCategory;
