/* eslint-disable indent */
import { useEffect, useState } from 'react';
import { useSelector } from 'react-redux';

import api from 'REST/Resources';
import { area, city } from 'Utils';
import { showMessage } from 'hooks';
import { getAuthLang } from 'store/auth/slice';
import { getTranslatedText } from 'components/local';

import { WrapContainer } from './styles';
import { InputLocation } from './input-location';
import { getDependWithLang } from './input-location/helpers';

const Location = ({ setLocationId, setLocationCurrent, onInputLocation }) => {
  const lang = useSelector(getAuthLang);

  const [location, setLocation] = useState(null);

  useEffect(() => {
    const getLocations = async () => {
      if (!location) {
        try {
          const locationToLang = await api.addGood.getLocationLanguageAll();
          const modifiedLocation = locationToLang
            .map((item) => getDependWithLang(item, lang))
            .map((item) =>
              item[area]
                ? item
                : {
                    ...item,
                    [area]: getTranslatedText('addAdv.districtKyiv'),
                  }
            );
          setLocation(modifiedLocation);
        } catch (e) {
          showMessage(e.response?.data?.error);
        }
      }
    };
    getLocations();
  }, []);

  return (
    <WrapContainer>
      <InputLocation
        name={area}
        location={location}
        setLocationId={setLocationId}
        title={getTranslatedText('addAdv.district')}
        inputLocation={onInputLocation.showLocation}
        setInputLocation={onInputLocation.setShowLocation}
      />
      <InputLocation
        name={city}
        location={location}
        setLocationId={setLocationId}
        setLocationCurrent={setLocationCurrent}
        title={getTranslatedText('addAdv.city')}
        inputLocation={onInputLocation.showLocation}
        setInputLocation={onInputLocation.setShowLocation}
      />
    </WrapContainer>
  );
};

export { Location };
