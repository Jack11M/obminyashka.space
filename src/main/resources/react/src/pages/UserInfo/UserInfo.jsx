import { useState } from 'react';
import { useNavigate } from 'react-router-dom';

import { Tabs } from './tabs';
import { Exit } from './tab_pages/exit';
import { ActiveProfile } from './active_profile';
import { RouterTabs } from './tab_pages/router_tabs';

import * as Styles from './styles';

const UserInfo = () => {
  const navigate = useNavigate();

  const [isModalOpen, onClose] = useState(false);
  const [prevLocation, setPrevLocation] = useState('');

  const close = () => {
    onClose(false);
    navigate(prevLocation);
  };

  return (
    <Styles.Container>
      <Styles.Aside>
        <ActiveProfile />

        <Tabs toggle={() => onClose(true)} />
      </Styles.Aside>

      <Styles.Main>
        <Styles.ContentWrapper>
          <RouterTabs set={[prevLocation, setPrevLocation]} />
        </Styles.ContentWrapper>
      </Styles.Main>

      {isModalOpen && <Exit toggle={close} onClose={onClose} />}
    </Styles.Container>
  );
};

export default UserInfo;
