import { useEffect } from 'react';
import { getUserThunk } from 'store/profile/thunk';
import { useDispatch, useSelector } from 'react-redux';

import { getProfile } from 'store/profile/slice';

import Sliders from './slider';
import HeaderInMain from './headerInMain';
import HelpChildren from './helpChildren';
import { CurrentOffers } from './currentOffers';

import * as Styles from './styles';

const HomePage = () => {
  const dispatch = useDispatch();
  const { avatarImage } = useSelector(getProfile);

  useEffect(() => {
    if (!avatarImage) {
      dispatch(getUserThunk());
    }
  }, [dispatch]);

  return (
    <Styles.Main>
      <HeaderInMain />

      <Styles.Container>
        <CurrentOffers />
        <Sliders />
        <HelpChildren />
      </Styles.Container>
    </Styles.Main>
  );
};

export default HomePage;
