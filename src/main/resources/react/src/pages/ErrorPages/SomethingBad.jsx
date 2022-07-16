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

import * as Styles from './styles';

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
      <Styles.WrapOrange>
        <img src={orangeDots} alt="orange dots" />
      </Styles.WrapOrange>

      <Styles.WrapGreen>
        <img src={greenDots} alt="green dots" />
      </Styles.WrapGreen>

      <Styles.WrapRight>
        <img src={loop} alt="loop" />
      </Styles.WrapRight>

      <Styles.WrapTittleBad>
        <Styles.TittleBad>
          {getTranslatedText('somethingBad.error', lang)}
        </Styles.TittleBad>

        <Styles.WrapperButton>
          <Styles.MainButton click={goTo}>
            {getTranslatedText('fourOhFour.mainPage', lang)}
          </Styles.MainButton>

          <Styles.BackButton click={goTo}>
            {getTranslatedText('fourOhFour.backPage', lang)}
          </Styles.BackButton>
        </Styles.WrapperButton>
      </Styles.WrapTittleBad>
    </Styles.Container>
  );
};
export default SomethingBad;
