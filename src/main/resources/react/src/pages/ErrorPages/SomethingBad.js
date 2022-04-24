import React from 'react';
import { useSelector } from 'react-redux';
import { useNavigate } from 'react-router-dom';

import { route } from 'routes/routeConstants';
import Button from 'components/common/buttons/button/Button';
import { getTranslatedText } from 'components/local/localisation';
import {
  loop,
  greenDots,
  orangeDots,
} from 'assets/img/all_images_export/errorPage';

import './somethingBad.scss';

const SomethingBad = ({ deactivateError }) => {
  const { lang } = useSelector((state) => state.auth);
  const navigate = useNavigate();

  const goTo = (event) => {
    deactivateError(false);
    if (event.target.className.includes('onMain')) {
      navigate(route.home);
    } else {
      navigate(-1);
    }
  };

  return (
    <div className="somethingBad">
      <div className="blockOrangeImage">
        <img src={orangeDots} alt="orange dots" />
      </div>
      <div className="blockGreenImage">
        <img src={greenDots} alt={'green dots'} />
      </div>
      <div className="blockRightImage">
        <img src={loop} alt={'loop'} />
      </div>
      <div className="blockControls">
        <h2>{getTranslatedText('somethingBad.error', lang)}</h2>
        <div className="blockButtons">
          <Button
            whatClass="onMain"
            text={getTranslatedText('fourOhFour.mainPage', lang)}
            click={goTo}
          />
          <Button
            whatClass="back"
            text={getTranslatedText('fourOhFour.backPage', lang)}
            click={goTo}
          />
        </div>
      </div>
    </div>
  );
};
export default SomethingBad;
