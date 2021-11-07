import React, { useState } from 'react';
import { useSelector } from 'react-redux';
import { useTransition, animated } from 'react-spring';
import { getTranslatedText } from 'components/local/localisation';

const Exchange = ({ exchangeList, setExchange }) => {
  const { lang } = useSelector((state) => state.auth);
  const [exchangeInput, setExchangeInput] = useState('');
  const [border, setBorder] = useState(false);

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
    if (!exchangeInput) return;
    if (event.which === 13) {
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
      <div
        className={border ? 'change_wrapper border_focus' : 'change_wrapper'}
      >
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
            onBlur={onBlur}
            onFocus={onFocus}
            value={exchangeInput}
            onKeyPress={keyEnter}
            onChange={handleInput}
          />
        </div>
      </div>
    </div>
  );
};

export { Exchange };
