import { useState } from 'react';
import { useSelector } from 'react-redux';
import { useNavigate } from 'react-router-dom';

import { Tabs } from './tabs';
import { Exit } from './tab_pages/exit';
import { ActiveProfile } from './active_profile';
import { RouterTabs } from './tab_pages/router_tabs';

import * as Styles from './styles';

const UserInfo = () => {
  const navigate = useNavigate();
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [prevLocation, setPrevLocation] = useState('');
  const { firstName, lastName, avatarImage } = useSelector(
    (state) => state.profileMe
  );

  const open = () => {
    setIsModalOpen(true);
  };

  const close = () => {
    setIsModalOpen(false);
    navigate(prevLocation);
  };

  return (
    <Styles.Container>
      <Styles.Aside>
        <ActiveProfile
          lastName={lastName}
          avatar={avatarImage}
          firstName={firstName}
        />
        <Tabs toggle={open} />
      </Styles.Aside>

      <Styles.Main>
        <Styles.ContentWrapper>
          <RouterTabs set={[prevLocation, setPrevLocation]} />
        </Styles.ContentWrapper>
      </Styles.Main>

      {isModalOpen && <Exit toggle={close} setIsModalOpen={setIsModalOpen} />}
    </Styles.Container>
  );
};

export default UserInfo;
