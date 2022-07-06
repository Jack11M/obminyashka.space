import { useSelector } from 'react-redux';
import { useNavigate } from 'react-router-dom';

import { getLang } from 'store/auth/slice';
import { route } from 'routes/routeConstants';
import { Button } from 'components/common/buttons';
import { getTranslatedText } from 'components/local/localization';
import {
  loop,
  greenDots,
  orangeDots,
} from 'assets/img/all_images_export/errorPage';

import {
  Container,
  WrapOImg,
  WrapGImg,
  WrapRImg,
  WrapTittle,
  Tittle,
  BlockButtons,
} from './styles-bad';

import './somethingBad.scss';

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
    <Container>
      <WrapOImg>
        <img src={orangeDots} alt="orange dots" />
      </WrapOImg>
      <WrapGImg>
        <img src={greenDots} alt="green dots" />
      </WrapGImg>
      <WrapRImg>
        <img src={loop} alt="loop" />
      </WrapRImg>
      <WrapTittle>
        <Tittle>{getTranslatedText('somethingBad.error', lang)}</Tittle>

        <BlockButtons>
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
        </BlockButtons>
      </WrapTittle>
    </Container>
  );
};
export default SomethingBad;
