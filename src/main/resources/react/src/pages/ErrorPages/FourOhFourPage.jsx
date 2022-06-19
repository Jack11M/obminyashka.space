import { useNavigate } from 'react-router-dom';

import { Button } from 'components/common';
import { route } from 'routes/routeConstants';
import { getTranslatedText } from 'components/local/localization';
import {
  loop,
  shadow,
  greenDots,
  fourOhFour,
  orangeDots,
  shadowDark,
} from 'assets/img/all_images_export/errorPage';

import './errorPage.scss';

const FourOhFourPage = () => {
  const navigate = useNavigate();

  const goTo = (event) => {
    if (event.target.className.includes('onMain')) {
      navigate(route.home);
    } else {
      navigate(-1);
    }
  };

  return (
    <div className="error-page">
      <div className="blockCenterImage">
        <div className="blockCenterImage-fourOhfour">
          <img src={fourOhFour} alt="404" />
        </div>

        <div className="blockCenterImage-shadow">
          <img
            className="blockCenterImage-shadow_light"
            src={shadow}
            alt="shadow"
          />
          <img
            className="blockCenterImage-shadow_dark"
            src={shadowDark}
            alt="shadow dark"
          />
        </div>
      </div>

      <div className="blockOrangeImage">
        <img src={orangeDots} alt="orange dots" />
      </div>

      <div className="blockGreenImage">
        <img src={greenDots} alt="green dots" />
      </div>

      <div className="blockRightImage">
        <img src={loop} alt="loop" />
      </div>

      <h2>{getTranslatedText('fourOhFour.noPage')}</h2>

      <div className="blockButtons">
        <Button
          whatClass="onMain"
          text={getTranslatedText('fourOhFour.mainPage')}
          click={goTo}
        />
        <Button
          whatClass="back"
          text={getTranslatedText('fourOhFour.backPage')}
          click={goTo}
        />
      </div>
    </div>
  );
};
export default FourOhFourPage;
