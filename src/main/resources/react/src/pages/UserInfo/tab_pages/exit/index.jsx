import React, { useCallback } from 'react';
import { useClickAway } from 'react-use';
import { useNavigate } from 'react-router-dom';
import { useDispatch, useSelector } from 'react-redux';

import { getLang } from 'store/auth/slice';
import { route } from 'routes/routeConstants';
import logout2 from 'assets/img/log-out-2.png';
import { logoutUserThunk } from 'store/auth/thunk';
import { Button } from 'components/common/buttons';
import { getTranslatedText } from 'components/local/localization';

import * as Styles from './styles';

const Exit = ({ toggle, setIsModalOpen }) => {
  const navigate = useNavigate();
  const dispatch = useDispatch();
  const ref = React.useRef(null);
  const lang = useSelector(getLang);

  const setLogOut = useCallback(async () => {
    await dispatch(logoutUserThunk());
    navigate(route.home);
  }, [dispatch, navigate]);

  useClickAway(ref, () => {
    setIsModalOpen(false);
  });
  return (
    <Styles.ModalOverlay className="modal-overlay">
      <Styles.Modal className="modal" ref={ref}>
        <Styles.ModalCross onClick={toggle} />

        <Styles.ModalTitle>
          {getTranslatedText('exit.question', lang)}
        </Styles.ModalTitle>

        <Styles.ModalText>
          {getTranslatedText('exit.text', lang)}
        </Styles.ModalText>

        <Styles.ButtonStyles>
          <Button
            width="179px"
            click={setLogOut}
            text={getTranslatedText('exit.exit', lang)}
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
