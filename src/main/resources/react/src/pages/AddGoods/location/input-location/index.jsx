import { useEffect, useMemo, useState } from 'react';

import api from 'REST/Resources';
import { showMessage } from 'hooks';
import { DropDownInput } from 'pages/AddGoods/drop-down-input';
import { getTranslatedText } from 'components/local/localization';

const InputLocation = ({
  lang,
  name,
  title,
  location,
  setLocation,
  setLocationId,
  inputLocation,
  setInputLocation,
  setLocationCurrent,
}) => {
  const langToUpperCase = useMemo(() => lang.toUpperCase(), [lang]);

  const [currLang, setCurrLang] = useState(langToUpperCase);
  const [showDrop, setShowDrop] = useState(false);
  const [filteredLocation, setFilteredLocation] = useState([]);
  const [uniqueLocation, setUniqueLocation] = useState([]);

  const getLocationId = (elem) => {
    const city =
      elem.indexOf('(') === -1
        ? elem
        : elem.substring(0, elem.indexOf('(') - 1);

    const district =
      elem.indexOf('(') === -1
        ? ''
        : elem.substring(elem.indexOf('(') + 1, elem.indexOf(')'));

    const finalLocation = location[langToUpperCase].find((item) => {
      if (district) return item.district === district && item.city === city;
      return item.city === city;
    });

    setLocationId(finalLocation?.id);
    setLocationCurrent(finalLocation);
  };

  useEffect(() => {
    if (!location[langToUpperCase].length) return;

    const uniqueField = () => {
      if (name === 'area') {
        return location[langToUpperCase]
          .map((item) => item[name])
          .filter((item, index, arr) => arr.indexOf(item) === index);
      }

      if (name === 'city') {
        const { area } = inputLocation;
        return location[langToUpperCase]
          .filter((curr) => curr.area === area)
          .map((item) =>
            item.district ? `${item[name]} (${item.district})` : item[name]
          );
      }
      return [];
    };

    setUniqueLocation(uniqueField());
  }, [location, inputLocation, langToUpperCase, name]);

  useEffect(() => {
    if (currLang !== langToUpperCase) {
      if (name === 'city') {
        setLocationId(null);
        setLocationCurrent(null);
      }

      setInputLocation({
        city: '',
        area: '',
      });

      setFilteredLocation([]);
      setShowDrop(false);
      setCurrLang(langToUpperCase);
    }
  }, [langToUpperCase]);

  useEffect(() => {
    if (inputLocation.area === '') setShowDrop(false);
  }, [inputLocation]);

  const getFiltered = (value) =>
    uniqueLocation.filter((item) =>
      item.toLowerCase().includes(value.toLowerCase())
    );

  const focus = async () => {
    if (!location[langToUpperCase].length) {
      try {
        const locationToLang = await api.fetchAddGood.getLocationLanguageAll();
        const modifiedLocation = locationToLang.map((item) =>
          item.area
            ? item
            : { ...item, area: getTranslatedText('addAdv.districtKyiv') }
        );
        setLocation({
          ...location,
          [modifiedLocation[0].i18N]: modifiedLocation,
        });
      } catch (e) {
        showMessage(e.response?.data?.error);
      }
    }
  };

  const handleInput = ({ target }) => {
    if (!target.value) setFilteredLocation([]);

    if (target.name === 'area') {
      setInputLocation((prevLocation) => ({
        ...prevLocation,
        city: '',
      }));
    }
    setFilteredLocation(getFiltered(target.value));
    setShowDrop(true);

    setInputLocation((prevLocation) => ({
      ...prevLocation,
      [name]: target.value,
    }));
  };

  const handleClick = (elem) => {
    if (name === 'city') getLocationId(elem);
    setInputLocation((prevLocation) => ({
      ...prevLocation,
      [name]: elem,
    }));
    setShowDrop(false);
  };

  const clearInput = () => {
    if (name === 'area') {
      setInputLocation({
        city: '',
        area: '',
      });
    } else {
      setInputLocation((prevLocation) => ({
        ...prevLocation,
        [name]: '',
      }));
    }

    setFilteredLocation([]);
    setShowDrop(false);
  };

  return (
    <DropDownInput
      name={name}
      title={title}
      onFocus={focus}
      focus={showDrop}
      showDrop={showDrop}
      clearInput={clearInput}
      data={filteredLocation}
      choiceItem={handleClick}
      value={inputLocation[name]}
      onChangeInput={handleInput}
      checkInputValue={inputLocation[name]}
      disabled={name !== 'area' && !inputLocation.area}
    />
  );
};
export { InputLocation };
