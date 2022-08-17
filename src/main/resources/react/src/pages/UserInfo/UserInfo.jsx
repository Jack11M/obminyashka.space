import { useEffect, useState } from 'react';
import { useDispatch } from 'react-redux';
import { useNavigate } from 'react-router-dom';

import { getUserThunk } from 'store/profile/thunk';

import { Tabs } from './tabs';
import { Exit } from './tab_pages/exit';
import { ActiveProfile } from './active_profile';
import { RouterTabs } from './tab_pages/router_tabs';

import * as Styles from './styles';

const UserInfo = () => {
  const dispatch = useDispatch();
  const navigate = useNavigate();
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [prevLocation, setPrevLocation] = useState('');

  const close = () => {
    setIsModalOpen(false);
    navigate(prevLocation);
  };

  useEffect(() => {
    dispatch(getUserThunk());
  }, [dispatch]);

  return (
    <Styles.Container>
      <Styles.Aside>
        <ActiveProfile />

        <Tabs toggle={() => setIsModalOpen(true)} />
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
