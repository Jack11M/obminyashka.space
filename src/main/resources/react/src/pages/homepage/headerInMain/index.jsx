import { useSelector } from 'react-redux';

import { getLang } from 'store/auth/slice';
import toys from 'assets/img/clouds/STEPthree.png';
import monitor from 'assets/img/clouds/STEPtwo.png';
import furniture from 'assets/img/clouds/STEP-one.png';
import { getTranslatedText } from 'components/local/localization';

import * as Styles from './styles';
import MoveCloud from './moveCloud';

const HeaderInMain = () => {
  const lang = useSelector(getLang);

  return (
    <Styles.PresentSection>
      <Styles.Wrapper>
        <Styles.PresentSlider>
          <Styles.PresentSliderElement>
            <Styles.FirstImage src={furniture} alt="furniture" />

            <Styles.TextWrapper>
              <Styles.FirstText>
                {getTranslatedText('mainPage.question', lang)}

                <b>{getTranslatedText('mainPage.questionBold', lang)}</b>
              </Styles.FirstText>
            </Styles.TextWrapper>
          </Styles.PresentSliderElement>

          <Styles.PresentSliderElement>
            <Styles.SecondImage src={monitor} alt="monitor" />

            <Styles.TextWrapper>
              <Styles.SecondText>
                {getTranslatedText('mainPage.answerFirstPart', lang)}
                &nbsp;
                <b>{getTranslatedText('mainPage.answerBold', lang)}</b>
                <br />
                {getTranslatedText('mainPage.answerSecondPart', lang)}
                <br />
                {getTranslatedText('mainPage.answerThirdPart', lang)}
              </Styles.SecondText>
            </Styles.TextWrapper>
          </Styles.PresentSliderElement>

          <Styles.PresentSliderElement>
            <Styles.ThirdImage src={toys} alt="toys" />

            <Styles.TextWrapper>
              <Styles.ThirdText>
                <b>{getTranslatedText('mainPage.changeBold', lang)}</b>
                &nbsp;
                {getTranslatedText('mainPage.changeFirstPart', lang)}
                <br />
                {getTranslatedText('mainPage.changeSecondPart', lang)}
                <br />
                {getTranslatedText('mainPage.changeThirdPart', lang)}
              </Styles.ThirdText>
            </Styles.TextWrapper>
          </Styles.PresentSliderElement>
        </Styles.PresentSlider>
      </Styles.Wrapper>

      <MoveCloud />
    </Styles.PresentSection>
  );
};

export default HeaderInMain;
