/* eslint-disable */
// @ts-nocheck
// TODO: fix typescript
import React, { useCallback } from 'react';
import { useClickAway } from 'react-use';
import { useDispatch } from 'react-redux';
import { useNavigate } from 'react-router-dom';
import { Button, Images } from 'obminyashka-components';

import { route } from 'src/routes/routeConstants';
import { logoutUserThunk } from 'src/store/auth/thunk';
import { clearProfile } from 'src/store/profile/slice';
import { getTranslatedText } from 'src/components/local/localization';

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

        <Styles.ModalTitle>{getTranslatedText('exit.question')}</Styles.ModalTitle>

        <Styles.ModalText>{getTranslatedText('exit.text')}</Styles.ModalText>

        <Styles.ButtonStyles>
          <Button width={179} onClick={setLogOut} text={getTranslatedText('exit.exit')} />
        </Styles.ButtonStyles>

        <Styles.ModalBackground>
          <Styles.ModalImage src={Images.logout2} alt='log-out' />
        </Styles.ModalBackground>
      </Styles.Modal>
    </Styles.ModalOverlay>
  );
};
export { Exit };
