import React from 'react';
import { useSelector } from 'react-redux';
import { useNavigate } from 'react-router-dom';

import { getLang } from 'store/auth/slice';
import { route } from 'routes/routeConstants';
import {
  loop,
  shadow,
  greenDots,
  fourOhFour,
  orangeDots,
  shadowDark,
} from 'assets/img/all_images_export/errorPage';
import Button from 'components/common/buttons/button/Button';
import { getTranslatedText } from 'components/local/localization';

import './errorPage.scss';

const FourOhFourPage = () => {
  const lang = useSelector(getLang);
  const navigate = useNavigate();

  const goTo = (event) => {
    if (event.target.className.includes('onMain')) {
      navigate(route.home);
    } else {
      navigate(-1);
    }
  };

  return (
    <div className={'error-page'}>
      <div className={'blockCenterImage'}>
        <div className={'blockCenterImage-fourOhfour'}>
          <img src={fourOhFour} alt={'404'} />
        </div>

        <div className={'blockCenterImage-shadow'}>
          <img
            className={'blockCenterImage-shadow_light'}
            src={shadow}
            alt={'shadow'}
          />
          <img
            className={'blockCenterImage-shadow_dark'}
            src={shadowDark}
            alt={'shadow dark'}
          />
        </div>
      </div>

      <div className={'blockOrangeImage'}>
        <img src={orangeDots} alt={'orange dots'} />
      </div>

      <div className={'blockGreenImage'}>
        <img src={greenDots} alt={'green dots'} />
      </div>

      <div className={'blockRightImage'}>
        <img src={loop} alt={'loop'} />
      </div>

      <h2>{getTranslatedText('fourOhFour.noPage', lang)}</h2>

      <div className={'blockButtons'}>
        <Button
          whatClass={'onMain'}
          text={getTranslatedText('fourOhFour.mainPage', lang)}
          click={goTo}
        />
        <Button
          whatClass={'back'}
          text={getTranslatedText('fourOhFour.backPage', lang)}
          click={goTo}
        />
      </div>
    </div>
  );
};
export default FourOhFourPage;
