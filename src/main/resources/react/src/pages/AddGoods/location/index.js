import { useState } from 'react';
import { useSelector } from 'react-redux';

import { getTranslatedText } from 'components/local/localisation.js';

import { InputLocation } from './input-location';

import { WrapContainer } from './styles.js';

const Location = ({ setLocationId, setLocationCurrent }) => {
  const { lang } = useSelector((state) => state.auth);

  const [inputLocation, setInputLocation] = useState({
    city: '',
    area: '',
  });

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
        inputLocation={inputLocation}
        setInputLocation={setInputLocation}
        title={getTranslatedText('addAdv.district', lang)}
      />
      <InputLocation
        lang={lang}
        name="city"
        location={location}
        setLocation={setLocation}
        inputLocation={inputLocation}
        setLocationId={setLocationId}
        setInputLocation={setInputLocation}
        setLocationCurrent={setLocationCurrent}
        title={getTranslatedText('addAdv.city', lang)}
      />
    </WrapContainer>
  );
};

export { Location };
