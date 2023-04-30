import { Images } from 'obminyashka-components';

import { getTranslatedText } from 'src/components/local/localization';

import * as Styles from './styles';
import MoveCloud from './moveCloud';

const HeaderInMain = () => {
  return (
    <Styles.PresentSection>
      <Styles.Wrapper>
        <Styles.PresentSlider>
          <Styles.PresentSliderElement>
            <Styles.FirstImage src={Images.stepOne} alt='furniture' />

            <Styles.TextWrapper>
              <Styles.FirstText>
                {getTranslatedText('mainPage.question')}

                <b>{getTranslatedText('mainPage.questionBold')}</b>
              </Styles.FirstText>
            </Styles.TextWrapper>
          </Styles.PresentSliderElement>

          <Styles.PresentSliderElement>
            <Styles.SecondImage src={Images.stepTwo} alt='monitor' />

            <Styles.TextWrapper>
              <Styles.SecondText>
                {getTranslatedText('mainPage.answerFirstPart')}
                &nbsp;
                <b>{getTranslatedText('mainPage.answerBold')}</b>
                <br />
                {getTranslatedText('mainPage.answerSecondPart')}
                <br />
                {getTranslatedText('mainPage.answerThirdPart')}
              </Styles.SecondText>
            </Styles.TextWrapper>
          </Styles.PresentSliderElement>

          <Styles.PresentSliderElement>
            <Styles.ThirdImage src={Images.stepThree} alt='toys' />

            <Styles.TextWrapper>
              <Styles.ThirdText>
                <b>{getTranslatedText('mainPage.changeBold')}</b>
                &nbsp;
                {getTranslatedText('mainPage.changeFirstPart')}
                <br />
                {getTranslatedText('mainPage.changeSecondPart')}
                <br />
                {getTranslatedText('mainPage.changeThirdPart')}
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
