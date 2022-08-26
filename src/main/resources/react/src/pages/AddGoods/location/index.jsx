import { useState } from 'react';
import { useSelector } from 'react-redux';

import { getAuthLang } from 'store/auth/slice';
import { getTranslatedText } from 'components/local/localization';

import { WrapContainer } from './styles';
import { InputLocation } from './input-location';

import * as Styles from '../styles';

const Location = ({ setLocationId, setLocationCurrent, onInputLocation }) => {
  const lang = useSelector(getAuthLang);

  const [location, setLocation] = useState({
    ua: [],
    en: [],
  });

  return (
    <WrapContainer>
      <Styles.Star>*</Styles.Star>
      <InputLocation
        lang={lang}
        name="area"
        location={location}
        setLocation={setLocation}
        inputLocation={onInputLocation.showLocation}
        setInputLocation={onInputLocation.setShowLocation}
        title={getTranslatedText('addAdv.district')}
      />

      <Styles.Star>*</Styles.Star>
      <InputLocation
        lang={lang}
        name="city"
        location={location}
        setLocation={setLocation}
        setLocationId={setLocationId}
        setLocationCurrent={setLocationCurrent}
        inputLocation={onInputLocation.showLocation}
        setInputLocation={onInputLocation.setShowLocation}
        title={getTranslatedText('addAdv.city')}
      />
    </WrapContainer>
  );
};

export { Location };
