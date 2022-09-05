import { useEffect, useState, useRef } from 'react';
import { useField } from 'formik';
import { useClickAway } from 'react-use';

import { DropDownInput } from 'pages/AddGoods/drop-down-input';

import { sortAlphabet } from './helpers';
import { area, city, district } from './config';

const InputLocation = ({
  name,
  title,
  location,
  setLocationId,
  inputLocation,
  setInputLocation,
  setLocationCurrent,
}) => {
  const inputRef = useRef();
  const [showDrop, setShowDrop] = useState(false);
  const [filteredLocation, setFilteredLocation] = useState([]);
  const [uniqueLocation, setUniqueLocation] = useState([]);

  const [, , helpers] = useField('locationId');

  useClickAway(inputRef, () => {
    setShowDrop(false);
  });

  const getLocationId = (elem) => {
    const cityCurr =
      elem.indexOf('(') === -1
        ? elem
        : elem.substring(0, elem.indexOf('(') - 1);

    const districtCurr =
      elem.indexOf('(') === -1
        ? ''
        : elem.substring(elem.indexOf('(') + 1, elem.indexOf(')'));

    const finalLocation = location.find((item) => {
      if (districtCurr) {
        return item[district] === districtCurr && item[city] === cityCurr;
      }
      return item[city] === cityCurr;
    });

    setLocationId(finalLocation?.id);
    setLocationCurrent(finalLocation);
  };

  useEffect(() => {
    if (!location) return;

    const uniqueField = () => {
      if (name === area) {
        return sortAlphabet(
          location
            .map((item) => item[name])
            .filter((item, index, arr) => arr.indexOf(item) === index)
        );
      }

      if (name === city) {
        const areaCurr = inputLocation?.[area];
        return sortAlphabet(
          location
            .filter((curr) => curr[area] === areaCurr)
            .map((item) =>
              item[district] ? `${item[name]} (${item[district]})` : item[name]
            )
        );
      }
      return [];
    };

    setUniqueLocation(uniqueField());
  }, [location, inputLocation, name]);

  useEffect(() => {
    if (inputLocation?.[area] === '') setShowDrop(false);
  }, [inputLocation]);

  const getFiltered = (value) => {
    return uniqueLocation.filter((item) =>
      item.split(' (')[0].toLowerCase().includes(value.toLowerCase())
    );
  };

  const handleInput = ({ target }) => {
    helpers.setError(undefined);

    if (!target.value) setFilteredLocation([]);

    if (target.name === area) {
      setInputLocation((prevLocation) => ({
        ...prevLocation,
        [city]: '',
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
    if (name === city) getLocationId(elem);

    setInputLocation((prevLocation) => ({
      ...prevLocation,
      [name]: elem,
    }));

    setShowDrop(false);
  };

  const clearInput = () => {
    if (name === area) {
      setInputLocation({
        [city]: '',
        [area]: '',
      });
    }
    if (name === city) {
      setInputLocation((prevLocation) => ({
        ...prevLocation,
        [name]: '',
      }));
    }

    setLocationId('');
    setShowDrop(false);
    setFilteredLocation([]);
  };

  const onFocus = () => {
    helpers.setError(undefined);
    setShowDrop(true);
  };

  return (
    <DropDownInput
      name={name}
      title={title}
      focus={showDrop}
      onFocus={onFocus}
      dropRef={inputRef}
      showDrop={showDrop}
      clearInput={clearInput}
      choiceItem={handleClick}
      onChangeInput={handleInput}
      value={inputLocation?.[name] || ''}
      checkInputValue={inputLocation?.[name]}
      disabled={name !== area && !inputLocation?.[area]}
      data={filteredLocation.length ? filteredLocation : uniqueLocation}
    />
  );
};
export { InputLocation };
