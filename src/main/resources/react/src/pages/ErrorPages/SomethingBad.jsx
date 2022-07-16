import { useSelector } from 'react-redux';
import { useNavigate } from 'react-router-dom';

import { getLang } from 'store/auth/slice';
import { route } from 'routes/routeConstants';
import { getTranslatedText } from 'components/local/localization';
import {
  loop,
  greenDots,
  orangeDots,
} from 'assets/img/all_images_export/errorPage';

import * as Styles from './styles-bad';

import './somethingBad.scss';
// import { BackButton, MainButton, WrapperButton } from './styled-error';

const SomethingBad = ({ deactivateError }) => {
  const lang = useSelector(getLang);
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
    <Styles.Container>
      <Styles.WrapOImg>
        <img src={orangeDots} alt="orange dots" />
      </Styles.WrapOImg>

      <Styles.WrapGImg>
        <img src={greenDots} alt="green dots" />
      </Styles.WrapGImg>

      <Styles.WrapRImg>
        <img src={loop} alt="loop" />
      </Styles.WrapRImg>

      <Styles.WrapTittle>
        <Styles.Tittle>
          {getTranslatedText('somethingBad.error', lang)}
        </Styles.Tittle>

        <Styles.WrapperButton>
          <Styles.MainButton click={goTo}>
            {getTranslatedText('fourOhFour.mainPage', lang)}
          </Styles.MainButton>

          <Styles.BackButton click={goTo}>
            {getTranslatedText('fourOhFour.backPage', lang)}
          </Styles.BackButton>
        </Styles.WrapperButton>
      </Styles.WrapTittle>
    </Styles.Container>
  );
};
export default SomethingBad;
