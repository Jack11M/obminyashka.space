import React, { useEffect, useState } from 'react';
import { useField } from 'formik';
import { useSelector } from 'react-redux';
import { useTransition, animated } from 'react-spring';

import { getTranslatedText } from 'components/local/localisation';

import { ErrorDisplay } from '../error-display';

const Exchange = ({ exchangeList, setExchange }) => {
  const { lang } = useSelector((state) => state.auth);
  const [exchangeInput, setExchangeInput] = useState('');
  const [border, setBorder] = useState(false);

  const [, meta, helpers] = useField({ name: 'wishesToExchange' });
  const { error } = meta;

  useEffect(() => {
    helpers.setError(undefined);
  }, [lang]);

  const transitions = useTransition(exchangeList.length ? exchangeList : [], {
    from: { opacity: 0, scale: 0 },
    enter: { opacity: 1, scale: 1 },
    leave: { opacity: 0, scale: 0 },
    config: { mass: 1, tension: 120, friction: 14, duration: 200 },
  });

  const handleInput = (event) => {
    setExchangeInput(event.target.value);
  };

  const keyEnter = (event) => {
    if (!exchangeInput) {
      if (event.key === 'Enter') event.preventDefault();
      return;
    }
    if (event.key === 'Enter') {
      event.preventDefault();
      setExchange((prev) => [...prev, exchangeInput]);
      setExchangeInput('');
    }
  };

  const removeExchangeItem = (text) => {
    const newExchangeList = exchangeList.filter((item) => item !== text);
    setExchange(newExchangeList);
  };

  const onFocus = () => {
    setBorder(true);
  };

  const onBlur = () => {
    setBorder(false);
  };
  const getBorderClassName = (border, error) => {
    if (border) return 'border_focus';
    if (error) return 'border_error';
    return '';
  };
  return (
    <div className="change">
      <h3 className="change_title">
        {getTranslatedText('addAdv.exchange', lang)}
      </h3>
      <p className="change-description">
        <span className="span_star">*</span>{' '}
        {getTranslatedText('addAdv.whatChange', lang)}
      </p>
      <p className="change-description_title">
        ({getTranslatedText('addAdv.enterPhrase', lang)})
      </p>
      <div className={`change_wrapper ${getBorderClassName(border, error)}`}>
        {transitions((styles, item) => (
          <animated.div key={item} style={{ ...styles }}>
            <div className="change_item">
              {item} <span onClick={() => removeExchangeItem(item)} />
            </div>
          </animated.div>
        ))}

        <div className="change_input-wrapper">
          <input
            type="text"
            onBlur={onBlur}
            onFocus={onFocus}
            value={exchangeInput}
            onKeyPress={keyEnter}
            onChange={handleInput}
            className="change_input"
            placeholder={getTranslatedText('addAdv.placeholderChange', lang)}
          />
        </div>
      </div>
      <ErrorDisplay error={!!error && error} />
    </div>
  );
};

export { Exchange };
