import { useEffect, useMemo, useState } from 'react';

import api from 'REST/Resources';
import { getTranslatedText } from 'components/local/localisation.js';

import { Label, Input, Wrap, SelectedItem, WrapDropItems } from './styles.js';

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
        const area = inputLocation.area;
        return location[langToUpperCase]
          .filter((curr) => curr.area === area)
          .map((item) =>
            item.district ? `${item[name]} (${item.district})` : item[name]
          );
      }
    };
    setUniqueLocation(uniqueField());
  }, [location, inputLocation]);

  useEffect(() => {
    if (name === 'city') {
      setLocationId(null);
      setLocationCurrent(null);
    }
    setInputLocation({
      city: '',
      area: '',
    });
    setFilteredLocation([]);
  }, [lang]);

  const getFiltered = (value) => {

    console.log(value);
    return uniqueLocation.filter((item) =>
      item.toLowerCase().includes(value.toLowerCase())
    );
  };

  const focus = async () => {
    if (!location[langToUpperCase].length) {
      try {
        const locationToLang = await api.fetchAddGood.getLocationLanguageAll();
        const modifiedLocation = locationToLang.map((item) =>
          item.area
            ? item
            : { ...item, area: getTranslatedText('addAdv.districtKyiv', lang) }
        );
        setLocation({
          ...location,
          [modifiedLocation[0].i18N]: modifiedLocation,
        });
      } catch (e) {
        console.log(e.response?.data?.error);
      }
    }
  };

  const handleInput = (e) => {
    const { name, value } = e.target;
    if (!value) setFilteredLocation([]);
    if (name === 'area') {
      setInputLocation((prevLocation) => ({
        ...prevLocation,
        city: '',
      }));
    }

    setFilteredLocation(getFiltered(value));
    setShowDrop(true);

    setInputLocation((prevLocation) => ({
      ...prevLocation,
      [name]: value,
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

  return (
    <Wrap>
      <Label>
        <span className="span_star">*</span> {title}:
      </Label>
      <Input
        name={name}
        focus={showDrop}
        onFocus={focus}
        value={inputLocation[name]}
        onChange={handleInput}
        autocomplete="off"
        disabled={name !== 'area' && !inputLocation.area}
      />

      {showDrop && (
        <WrapDropItems>
          {filteredLocation.map((item, index) => (
            <SelectedItem
              key={`${item}_${index}`}
              onClick={() => handleClick(item)}
            >
              <p>{item}</p>
            </SelectedItem>
          ))}
        </WrapDropItems>
      )}
    </Wrap>
  );
};
export { InputLocation };
