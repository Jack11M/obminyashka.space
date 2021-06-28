import React from 'react';
import { useSelector } from 'react-redux';

import { getTranslatedText } from '../../../components/local/localisation';
import furniture from '../../../img/clouds/STEP-one.png';
import monitor from '../../../img/clouds/STEPtwo.png';
import toys from '../../../img/clouds/STEPthree.png';

import './headerInMain.scss';
import './moveCloud.scss';

const HeaderInMain = () => {
  const { lang } = useSelector((state) => state.auth);

  return (
    <div className="present-section">
      <div className="wrapper">
        <ul className="presentSlider">
          <li className="presentSlider-element">
            <img src={furniture} alt="furniture" />
            <p className="presentSlider-element__text">
              {getTranslatedText('mainPage.question', lang)}
              <b> {getTranslatedText('mainPage.questionBold', lang)}</b>
            </p>
          </li>
          <li className="presentSlider-element">
            <img src={monitor} alt="monitor" />
            <p className="presentSlider-element__text">
              {getTranslatedText('mainPage.answerFirstPart', lang)}{' '}
              <b>{getTranslatedText('mainPage.answerBold', lang)}</b>
              <br />
              {getTranslatedText('mainPage.answerSecondPart', lang)}
              <br />
              {getTranslatedText('mainPage.answerThirdPart', lang)}
            </p>
          </li>
          <li className="presentSlider-element ">
            <img src={toys} alt="toys" />
            <p className="presentSlider-element__text">
              <b>{getTranslatedText('mainPage.changeBold', lang)}</b>{' '}
              {getTranslatedText('mainPage.changeFirstPart', lang)}
              <br />
              {getTranslatedText('mainPage.changeSecondPart', lang)}
              <br />
              {getTranslatedText('mainPage.changeThirdPart', lang)}
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
};

export default HeaderInMain;
