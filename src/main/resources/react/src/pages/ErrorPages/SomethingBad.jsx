import { useSelector } from 'react-redux';
import { useNavigate } from 'react-router-dom';

import { getLang } from 'store/auth/slice';
import { getTranslatedText } from 'components/local/localization';
import {
  loop,
  greenDots,
  orangeDots,
} from 'assets/img/all_images_export/errorPage';

import { goTo } from './helpers';
import * as Styles from './styles';

const SomethingBad = ({ deactivateError }) => {
  const lang = useSelector(getLang);
  const navigate = useNavigate();

  return (
    <Styles.Container>
      <Styles.WrapOrange>
        <Styles.Image src={orangeDots} alt="orange dots" />
      </Styles.WrapOrange>

      <Styles.WrapGreen>
        <Styles.Image src={greenDots} alt="green dots" />
      </Styles.WrapGreen>

      <Styles.WrapRight>
        <Styles.Image src={loop} alt="loop" />
      </Styles.WrapRight>

      <Styles.WrapTittleBad>
        <Styles.TittleBad>
          {getTranslatedText('somethingBad.error', lang)}
        </Styles.TittleBad>

        <Styles.WrapperButton>
          <Styles.MainButton
            onClick={() => goTo('home', navigate, deactivateError)}
          >
            {getTranslatedText('fourOhFour.mainPage', lang)}
          </Styles.MainButton>

          <Styles.BackButton
            onClick={() => goTo('', navigate, deactivateError)}
          >
            {getTranslatedText('fourOhFour.backPage', lang)}
          </Styles.BackButton>
        </Styles.WrapperButton>
      </Styles.WrapTittleBad>
    </Styles.Container>
  );
};
export default SomethingBad;
