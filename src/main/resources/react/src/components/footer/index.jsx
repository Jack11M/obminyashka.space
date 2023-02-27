/* eslint-disable max-len */
import { Button, Icon } from '@wolshebnik/obminyashka-components';

import { route } from 'routes/routeConstants';

import logoFooter from 'assets/img/Logo-footer.png';
import { getTranslatedText } from 'components/local/localization';

import * as Styles from './styles';

const Footer = () => {
  const timeDate = new Date();
  const yearNow = timeDate.getFullYear();

  return (
    <Styles.Container>
      <Styles.Wrapper>
        <Styles.Blocks>
          <Styles.Lists>
            <Styles.Span>
              <Icon.Phone />
            </Styles.Span>

            <Styles.Contact href="mailto:&#111;&#098;&#109;&#105;&#110;&#121;&#097;&#115;&#104;&#107;&#097;&#046;&#115;&#112;&#097;&#099;&#101;&#064;&#103;&#109;&#097;&#105;&#108;&#046;&#099;&#111;&#109;">
              &#111;&#098;&#109;&#105;&#110;&#121;&#097;&#115;&#104;&#107;&#097;&#046;&#115;&#112;&#097;&#099;&#101;&#064;&#103;&#109;&#097;&#105;&#108;&#046;&#099;&#111;&#109;
            </Styles.Contact>

            <Styles.WrapContacts>
              <Styles.Contact href="tel:&#43;&#51;&#56;&#48;&#57;&#51;&#49;&#50;&#51;&#52;&#53;&#54;&#55;">
                +3 80 (93) 123 45 67
              </Styles.Contact>

              <Styles.Contact href="tel:&#43;&#51;&#56;&#48;&#57;&#51;&#49;&#50;&#51;&#52;&#53;&#54;&#55;">
                +3 80 (93) 123 45 67
              </Styles.Contact>
            </Styles.WrapContacts>
          </Styles.Lists>

          <Styles.Lists>
            <Styles.Span>
              <Icon.QuestionMark />
            </Styles.Span>

            <Styles.FootLinkWrapper rules>
              <Styles.FootLink to={route.home}>
                {getTranslatedText('footer.rules')}
              </Styles.FootLink>
            </Styles.FootLinkWrapper>

            <Styles.FootLinkWrapper rules>
              <Styles.FootLink to={route.home}>
                {getTranslatedText('footer.charity')}
              </Styles.FootLink>
            </Styles.FootLinkWrapper>

            <Styles.FootLinkWrapper rules>
              <Styles.FootLink to={route.home}>
                {getTranslatedText('footer.questions')}
              </Styles.FootLink>
            </Styles.FootLinkWrapper>
          </Styles.Lists>

          <Styles.Lists>
            <Styles.Span>
              <Icon.Home />
            </Styles.Span>

            <Styles.FootLink to={route.home}>
              <Styles.Img src={logoFooter} alt="Logo" />
            </Styles.FootLink>

            <Button
              width={155}
              icon={<Icon.Heart />}
              text={getTranslatedText('header.goodness')}
              style={{
                height: 22,
                whiteSpace: 'nowrap',
              }}
            />
          </Styles.Lists>
        </Styles.Blocks>
      </Styles.Wrapper>

      <Styles.CopyContainer>
        <Styles.SpanCopy>
          &copy;
          {getTranslatedText('footer.protect')}
        </Styles.SpanCopy>

        <Styles.SpanCopy>
          {`${yearNow} / ${getTranslatedText('mainPage.helpName')}`}
        </Styles.SpanCopy>
      </Styles.CopyContainer>
    </Styles.Container>
  );
};

export default Footer;
