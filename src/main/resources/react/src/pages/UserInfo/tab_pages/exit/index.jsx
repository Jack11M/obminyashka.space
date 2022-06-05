import React, { useCallback } from 'react';
import { useClickAway } from 'react-use';
import { useDispatch } from 'react-redux';
import { useNavigate } from 'react-router-dom';

import { route } from 'routes/routeConstants';
import logout2 from 'assets/img/log-out-2.png';
import { logoutUserThunk } from 'store/auth/thunk';
import { Button } from 'components/common/buttons';
import { getTranslatedText } from 'components/local/localization';

import './exit.scss';

const Exit = ({ toggle, setIsModalOpen }) => {
  const navigate = useNavigate();
  const dispatch = useDispatch();
  const ref = React.useRef(null);

  const setLogOut = useCallback(async () => {
    await dispatch(logoutUserThunk());
    navigate(route.home);
  }, [dispatch, navigate]);

  useClickAway(ref, () => {
    setIsModalOpen(false);
  });
  return (
    <div className="modal-overlay">
      <div className="modal" ref={ref}>
        <div onClick={toggle} className="modal__cross js-modal-close" />

        <p className="modal__title">{getTranslatedText('exit.question')}</p>

        <p className="modal__text">{getTranslatedText('exit.text')}</p>

        <Button
          whatClass="button"
          text={getTranslatedText('exit.exit')}
          width="179px"
          click={setLogOut}
        />
        <div className="background">
          <img src={logout2} className="log-out-img" alt="log-out" />
        </div>
      </div>
    </div>
  );
};
export { Exit };
