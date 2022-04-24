import { useState } from 'react';
import { useSelector } from 'react-redux';

import { getTranslatedText } from 'components/local/localization.js';

import { InputLocation } from './input-location';

import { WrapContainer } from './styles.js';

const Location = ({ setLocationId, setLocationCurrent, onInputLocation }) => {
  const { lang } = useSelector((state) => state.auth);

  const [location, setLocation] = useState({
    RU: [],
    UA: [],
    EN: [],
  });

  return (
    <WrapContainer>
      <InputLocation
        lang={lang}
        name="area"
        location={location}
        setLocation={setLocation}
        inputLocation={onInputLocation.showLocation}
        setInputLocation={onInputLocation.setShowLocation}
        title={getTranslatedText('addAdv.district', lang)}
      />

      <InputLocation
        lang={lang}
        name="city"
        location={location}
        setLocation={setLocation}
        setLocationId={setLocationId}
        setLocationCurrent={setLocationCurrent}
        inputLocation={onInputLocation.showLocation}
        setInputLocation={onInputLocation.setShowLocation}
        title={getTranslatedText('addAdv.city', lang)}
      />
    </WrapContainer>
  );
};

export { Location };
