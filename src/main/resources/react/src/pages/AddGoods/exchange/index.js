import React, { useState } from 'react';
import { useSelector } from 'react-redux';
import { useTransition, animated } from 'react-spring';
import { getTranslatedText } from 'components/local/localisation';

const Exchange = ({ data, setExchange }) => {
  const { lang } = useSelector((state) => state.auth);
  const [exchangeInput, setExchangeInput] = useState('');

  const transitions = useTransition(data.length ? data : [], {
    from: { opacity: 0, scale: 0 },
    enter: { opacity: 1, scale: 1 },
    leave: { opacity: 0, scale: 0 },
    config: { mass: 1, tension: 120, friction: 14, duration: 200 },
  });

  const handleInput = (event) => {
    setExchangeInput(event.target.value);
  };

  const keyEnter = (event) => {
    if (!exchangeInput) return;
    if (event.which === 13) {
      setExchange((prev) => [...prev, exchangeInput]);
      setExchangeInput('');
    }
  };

  const removeExchangeItem = (text) => {
    const newExchangeList = data.filter((item) => item !== text);
    setExchange(newExchangeList);
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
      <div className="change_wrapper">
        {transitions((styles, item) => (
          <animated.div key={item} style={{ ...styles }}>
            <div className="change_item">
              {item} <span onClick={() => removeExchangeItem(item)} />
            </div>
          </animated.div>
        ))}

        <div className="change_input-wrapper">
          <input
            className="change_input"
            type="text"
            placeholder={getTranslatedText('addAdv.placeholderChange', lang)}
            onChange={handleInput}
            value={exchangeInput}
            onKeyPress={keyEnter}
          />
        </div>
      </div>
    </div>
  );
};

export { Exchange };
