/* eslint-disable max-len */

import { Link } from 'react-router-dom';

import { route } from 'routes/routeConstants';
import logoFooter from 'assets/img/Logo-footer.png';
import { BtnGoodBusiness } from 'components/common';
import { ReactComponent as HomeSvg } from 'assets/icons/home.svg';
import { getTranslatedText } from 'components/local/localization';
import { ReactComponent as PhoneSvg } from 'assets/icons/phone.svg';
import { ReactComponent as QuestionSvg } from 'assets/icons/question-mark.svg';

import './Footer.scss';

const Footer = () => {
  const timeDate = new Date();
  const yearNow = timeDate.getFullYear();

  return (
    <footer className="HomePageFooter">
      <div className="wrapper">
        <ul className="footer-blocks">
          <li className="footer-list">
            <span className="footer-list__icon">
              <PhoneSvg />
            </span>

            <a href="mailto:&#111;&#098;&#109;&#105;&#110;&#121;&#097;&#115;&#104;&#107;&#097;&#046;&#115;&#112;&#097;&#099;&#101;&#064;&#103;&#109;&#097;&#105;&#108;&#046;&#099;&#111;&#109;">
              &#111;&#098;&#109;&#105;&#110;&#121;&#097;&#115;&#104;&#107;&#097;&#046;&#115;&#112;&#097;&#099;&#101;&#064;&#103;&#109;&#097;&#105;&#108;&#046;&#099;&#111;&#109;
            </a>
            <div className="footer-list-tel">
              <a href="tel:&#43;&#51;&#56;&#48;&#57;&#51;&#49;&#50;&#51;&#52;&#53;&#54;&#55;">
                +3 80 (93) 123 45 67
              </a>
              <a href="tel:&#43;&#51;&#56;&#48;&#57;&#51;&#49;&#50;&#51;&#52;&#53;&#54;&#55;">
                +3 80 (93) 123 45 67
              </a>
            </div>
          </li>

          <li className="footer-list">
            <span className="footer-list__icon">
              <QuestionSvg />
            </span>

            <Link to={route.home} className="footer-list_rules">
              {getTranslatedText('footer.rules')}
            </Link>

            <Link to={route.home} className="footer-list_rules">
              {getTranslatedText('footer.charity')}
            </Link>

            <Link to={route.home} className="footer-list_rules">
              {getTranslatedText('footer.questions')}
            </Link>
          </li>

          <li className="footer-list">
            <span className="footer-list__icon">
              <HomeSvg />
            </span>

            <Link to={route.home}>
              <img src={logoFooter} alt="Logo" />
            </Link>

            <BtnGoodBusiness
              href="#"
              whatClass="btn-Help-Children"
              text={getTranslatedText('header.goodness')}
            />
          </li>
        </ul>
      </div>

      <div className="copyright">
        <span>
          &copy;
          {getTranslatedText('footer.protect')}
        </span>
        <span id="dataYear">{`${yearNow} / Обменяшка`}</span>
      </div>
    </footer>
  );
};

export default Footer;
