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
import { getTranslatedText } from 'components/local/localization';

// import './errorPage.scss';
import {
  Tittle,
  WrapOImg,
  WrapGImg,
  WrapRImg,
  WrapCenter,
  WrapImg404,
  MainButton,
  BackButton,
  WrapperDark,
  WrapperLight,
  WrapperShadow,
  WrapperButton,
} from './styled-error';

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
    <div className="error-page">
      <WrapCenter>
        <WrapImg404>
          <img src={fourOhFour} alt="404" />
        </WrapImg404>

        <WrapperShadow>
          <WrapperLight src={shadow} alt="shadow" />
          <WrapperDark src={shadowDark} alt="shadow dark" />
        </WrapperShadow>
      </WrapCenter>

      <WrapOImg>
        <img src={orangeDots} alt="orange dots" />
      </WrapOImg>

      <WrapGImg>
        <img src={greenDots} alt="green dots" />
      </WrapGImg>

      <WrapRImg>
        <img src={loop} alt="loop" />
      </WrapRImg>

      <Tittle>{getTranslatedText('fourOhFour.noPage', lang)}</Tittle>

      <WrapperButton>
        <MainButton
          whatClass="onMain"
          text={getTranslatedText('fourOhFour.mainPage', lang)}
          click={goTo}
        />

        <BackButton
          whatClass="back"
          text={getTranslatedText('fourOhFour.backPage', lang)}
          click={goTo}
        />
      </WrapperButton>
    </div>
  );
};
export default FourOhFourPage;
