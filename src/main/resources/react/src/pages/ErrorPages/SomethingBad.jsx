import { useNavigate } from 'react-router-dom';
import { Images } from 'obminyashka-components';

import { getTranslatedText } from 'components/local/localization';

import { goTo } from './helpers';
import * as Styles from './styles';

const SomethingBad = ({ deactivateError }) => {
  const navigate = useNavigate();

  return (
    <Styles.Container>
      <Styles.WrapOrange>
        <Styles.Image src={Images.orangeDots} alt="orange dots" />
      </Styles.WrapOrange>

      <Styles.WrapGreen>
        <Styles.Image src={Images.greenDots} alt="green dots" />
      </Styles.WrapGreen>

      <Styles.WrapRight>
        <Styles.Image src={Images.loop} alt="loop" />
      </Styles.WrapRight>

      <Styles.WrapTittleBad>
        <Styles.TittleBad>
          {getTranslatedText('somethingBad.error')}
        </Styles.TittleBad>

        <Styles.WrapperButton>
          <Styles.MainButton
            onClick={() => goTo('home', navigate, deactivateError)}
          >
            {getTranslatedText('fourOhFour.mainPage')}
          </Styles.MainButton>

          <Styles.BackButton
            onClick={() => goTo('', navigate, deactivateError)}
          >
            {getTranslatedText('fourOhFour.backPage')}
          </Styles.BackButton>
        </Styles.WrapperButton>
      </Styles.WrapTittleBad>
    </Styles.Container>
  );
};
export default SomethingBad;
