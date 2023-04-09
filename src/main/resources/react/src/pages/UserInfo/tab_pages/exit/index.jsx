import { useCallback } from 'react';
// import { useClickAway } from 'react-use';
import { useDispatch } from 'react-redux';
import { useNavigate } from 'react-router-dom';
import { Button, Modal } from 'obminyashka-components';

import { route } from 'routes/routeConstants';
import logout2 from 'assets/img/log-out-2.png';
import { logoutUserThunk } from 'store/auth/thunk';
import { clearProfile } from 'store/profile/slice';
import { getTranslatedText } from 'components/local/localization';

import * as Styles from './styles';

const Exit = ({ toggle, onClose }) => {
  const navigate = useNavigate();
  const dispatch = useDispatch();
  // const ref = React.useRef(null);

  const setLogOut = useCallback(async () => {
    await dispatch(logoutUserThunk());
    dispatch(clearProfile());
    navigate(route.home);
  }, [dispatch, navigate]);

  // useClickAway(ref, () => {
  //   onClose(false);
  //   toggle();
  // });

  return (
    <Modal isOpen={toggle} onClose={onClose} duration={1000}>
      <Styles.ModalTitle>
        {getTranslatedText('exit.question')}
      </Styles.ModalTitle>

      <Styles.ModalText>{getTranslatedText('exit.text')}</Styles.ModalText>

      <Styles.ButtonStyles>
        <Button
          width={180}
          onClick={setLogOut}
          text={getTranslatedText('exit.exit')}
        />
      </Styles.ButtonStyles>

      <Styles.ModalBackground>
        <Styles.ModalImage src={logout2} alt="log-out" />
      </Styles.ModalBackground>
    </Modal>
  );
};
export { Exit };
