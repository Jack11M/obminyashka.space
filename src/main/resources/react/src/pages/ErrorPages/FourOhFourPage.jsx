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

import * as Styles from './styled-error';

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
    <>
      <Styles.WrapCenter>
        <Styles.WrapImg404>
          <img src={fourOhFour} alt="404" />
        </Styles.WrapImg404>

        <Styles.WrapperShadow>
          <Styles.WrapperLight src={shadow} alt="shadow" />
          <Styles.WrapperDark src={shadowDark} alt="shadow dark" />
        </Styles.WrapperShadow>
      </Styles.WrapCenter>

      <Styles.WrapOImg>
        <img src={orangeDots} alt="orange dots" />
      </Styles.WrapOImg>

      <Styles.WrapGImg>
        <img src={greenDots} alt="green dots" />
      </Styles.WrapGImg>

      <Styles.WrapRImg>
        <img src={loop} alt="loop" />
      </Styles.WrapRImg>

      <Styles.Tittle>
        {getTranslatedText('fourOhFour.noPage', lang)}
      </Styles.Tittle>

      <Styles.WrapperButton>
        <Styles.MainButton click={goTo}>
          {getTranslatedText('fourOhFour.mainPage', lang)}
        </Styles.MainButton>

        <Styles.BackButton click={goTo}>
          {getTranslatedText('fourOhFour.backPage', lang)}
        </Styles.BackButton>
      </Styles.WrapperButton>
    </>
  );
};
export default FourOhFourPage;
