import { useState } from 'react';
import { useSelector } from 'react-redux';
import { useNavigate } from 'react-router-dom';

import { getProfile } from 'store/profile/slice';

import { Tabs } from './tabs';
import { Exit } from './tab_pages/exit';
import { ActiveProfile } from './active_profile';
import { RouterTabs } from './tab_pages/router_tabs';

import './UserInfo.scss';

const UserInfo = () => {
  const navigate = useNavigate();
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [prevLocation, setPrevLocation] = useState('');
  const { firstName, lastName, avatarImage } = useSelector(getProfile);

  const open = () => {
    setIsModalOpen(true);
  };

  const close = () => {
    setIsModalOpen(false);
    navigate(prevLocation);
  };

  return (
    <div className="container">
      <aside className="left-side">
        <ActiveProfile
          lastName={lastName}
          avatar={avatarImage}
          firstName={firstName}
        />
        <Tabs toggle={open} />
      </aside>

      <main className="main-content">
        <div className="main-content-wrapper">
          <RouterTabs set={[prevLocation, setPrevLocation]} />
        </div>
      </main>

      {isModalOpen && <Exit toggle={close} setIsModalOpen={setIsModalOpen} />}
    </div>
  );
};

export default UserInfo;
