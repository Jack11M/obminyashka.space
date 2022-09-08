import React, { useCallback } from 'react';
import { useClickAway } from 'react-use';
import { useDispatch } from 'react-redux';
import { useNavigate } from 'react-router-dom';

import { Button } from 'components/common';
import { route } from 'routes/routeConstants';
import logout2 from 'assets/img/log-out-2.png';
import { clearProfile } from 'store/profile/slice';
import { logoutUserThunk } from 'store/auth/thunk';
import { getTranslatedText } from 'components/local/localization';

import * as Styles from './styles';

const Exit = ({ toggle, setIsModalOpen }) => {
  const navigate = useNavigate();
  const dispatch = useDispatch();
  const ref = React.useRef(null);

  const setLogOut = useCallback(async () => {
    await dispatch(logoutUserThunk());
    dispatch(clearProfile());
    navigate(route.home);
  }, [dispatch, navigate]);

  useClickAway(ref, () => {
    setIsModalOpen(false);
    toggle();
  });

  return (
    <Styles.ModalOverlay>
      <Styles.Modal ref={ref}>
        <Styles.ModalCross onClick={toggle} />

        <Styles.ModalTitle>
          {getTranslatedText('exit.question')}
        </Styles.ModalTitle>

        <Styles.ModalText>{getTranslatedText('exit.text')}</Styles.ModalText>

        <Styles.ButtonStyles>
          <Button
            width="179px"
            click={setLogOut}
            text={getTranslatedText('exit.exit')}
          />
        </Styles.ButtonStyles>

        <Styles.ModalBackground>
          <Styles.ModalImage src={logout2} alt="log-out" />
        </Styles.ModalBackground>
      </Styles.Modal>
    </Styles.ModalOverlay>
  );
};
export { Exit };
