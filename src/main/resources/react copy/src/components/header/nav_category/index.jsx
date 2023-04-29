import { Images } from 'obminyashka-components';

import { route } from 'routes/routeConstants';
import { getTranslatedText } from 'components/local/localization';

import * as Styles from './styles';

const NavCategory = () => {
  return (
    <Styles.List>
      <Styles.Wrapper>
        <Styles.NavbarLinkContainer>
          <Styles.NavbarLink to={route.home}>
            <Styles.Img src={Images.clothes} alt="clothes" />
            <Styles.Span>{getTranslatedText('header.clothes')}</Styles.Span>
          </Styles.NavbarLink>
        </Styles.NavbarLinkContainer>

        <Styles.NavbarLinkContainer>
          <Styles.NavbarLink to={route.home}>
            <Styles.Img src={Images.shoes} alt="shoes" />
            <Styles.Span>{getTranslatedText('header.shoes')}</Styles.Span>
          </Styles.NavbarLink>
        </Styles.NavbarLinkContainer>

        <Styles.NavbarLinkContainer>
          <Styles.NavbarLink to={route.home}>
            <Styles.Img src={Images.toys} alt="toys" />
            <Styles.Span>{getTranslatedText('header.toys')}</Styles.Span>
          </Styles.NavbarLink>
        </Styles.NavbarLinkContainer>

        <Styles.NavbarLinkContainer>
          <Styles.NavbarLink to={route.home}>
            <Styles.Img
              src={Images.transportForChildren}
              alt="transportForChildren"
            />
            <Styles.Span>{getTranslatedText('header.vehicles')}</Styles.Span>
          </Styles.NavbarLink>
        </Styles.NavbarLinkContainer>

        <Styles.NavbarLinkContainer>
          <Styles.NavbarLink to={route.home}>
            <Styles.Img src={Images.furniture} alt="furniture" />
            <Styles.Span>{getTranslatedText('header.furniture')}</Styles.Span>
          </Styles.NavbarLink>
        </Styles.NavbarLinkContainer>

        <Styles.NavbarLinkContainer>
          <Styles.NavbarLink to={route.home}>
            <Styles.Img src={Images.kidsUpToYear} alt="kidsUpToYear" />
            <Styles.Span>{getTranslatedText('header.babies')}</Styles.Span>
          </Styles.NavbarLink>
        </Styles.NavbarLinkContainer>

        <Styles.NavbarLinkContainer>
          <Styles.NavbarLink to={route.home}>
            <Styles.Img src={Images.books} alt="books" />
            <Styles.Span>{getTranslatedText('header.books')}</Styles.Span>
          </Styles.NavbarLink>
        </Styles.NavbarLinkContainer>

        <Styles.NavbarLinkContainer>
          <Styles.NavbarLink to={route.home}>
            <Styles.Img src={Images.other} alt="other" />
            <Styles.Span>{getTranslatedText('header.another')}</Styles.Span>
          </Styles.NavbarLink>
        </Styles.NavbarLinkContainer>
      </Styles.Wrapper>
    </Styles.List>
  );
};

export default NavCategory;
