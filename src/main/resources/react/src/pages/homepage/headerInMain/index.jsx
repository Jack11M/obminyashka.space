import toys from 'assets/img/clouds/STEPthree.png';
import monitor from 'assets/img/clouds/STEPtwo.png';
import furniture from 'assets/img/clouds/STEP-one.png';
import { getTranslatedText } from 'components/local/localization';

import './headerInMain.scss';
import './moveCloud.scss';

const HeaderInMain = () => (
  <div className="present-section">
    <div className="wrapper">
      <ul className="presentSlider">
        <li className="presentSlider-element">
          <img src={furniture} alt="furniture" />
          <p className="presentSlider-element__text">
            {getTranslatedText('mainPage.question')}
            <b>{getTranslatedText('mainPage.questionBold')}</b>
          </p>
        </li>
        <li className="presentSlider-element">
          <img src={monitor} alt="monitor" />
          <p className="presentSlider-element__text">
            {getTranslatedText('mainPage.answerFirstPart')}
            &nbsp;
            <b>{getTranslatedText('mainPage.answerBold')}</b>
            <br />
            {getTranslatedText('mainPage.answerSecondPart')}
            <br />
            {getTranslatedText('mainPage.answerThirdPart')}
          </p>
        </li>

        <li className="presentSlider-element ">
          <img src={toys} alt="toys" />
          <p className="presentSlider-element__text">
            <b>{getTranslatedText('mainPage.changeBold')}</b>
            &nbsp;
            {getTranslatedText('mainPage.changeFirstPart')}
            <br />
            {getTranslatedText('mainPage.changeSecondPart')}
            <br />
            {getTranslatedText('mainPage.changeThirdPart')}
          </p>
        </li>
      </ul>
    </div>

    <div className="cloudsInner">
      <span className="cloudOne" />
      <span className="cloudTwo" />
      <span className="cloudThree" />
      <span className="cloudFour" />
      <span className="cloudFive" />
      <span className="cloudSix" />
    </div>
  </div>
);

export default HeaderInMain;
