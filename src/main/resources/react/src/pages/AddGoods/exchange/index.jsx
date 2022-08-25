import { useState } from 'react';
import { useField } from 'formik';
import { useTransition, animated } from 'react-spring';

import { FormikCheckBox } from 'components/common/formik';
import { ErrorDisplay } from 'pages/AddGoods/error-display';
import { getTranslatedText } from 'components/local/localization';

const Exchange = ({ exchangeList, setExchange, readyOffers }) => {
  const [exchangeInput, setExchangeInput] = useState('');
  const [border, setBorder] = useState(false);

  const [, meta, helpers] = useField({ name: 'wishesToExchange' });
  const { error } = meta;

  const transitions = useTransition(exchangeList.length ? exchangeList : [], {
    from: { opacity: 0, scale: 0 },
    enter: { opacity: 1, scale: 1 },
    leave: { opacity: 0, scale: 0 },
    config: { mass: 1, tension: 120, friction: 14, duration: 200 },
  });

  const handleInput = (event) => {
    if (event.target.value.length >= 40) {
      helpers.setError(getTranslatedText('errors.max40'));
      return;
    }
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

  const getBorderClassName = (borderValue, errorValue) => {
    if (borderValue) return 'border_focus';
    if (errorValue) return 'border_error';
    return '';
  };

  return (
    <div className="change">
      <h3 className="change_title">
        {getTranslatedText('addAdv.exchange')}{' '}
        <span className="span_star">*</span>
      </h3>

      <p className="change-description">
        &nbsp;
        {getTranslatedText('addAdv.whatChange')}
      </p>

      <p className="change-description_title">
        ({getTranslatedText('addAdv.enterPhrase')})
      </p>

      <div className={`change_wrapper ${getBorderClassName(border, error)}`}>
        {transitions((styles, item) => (
          <animated.div key={item} style={{ ...styles }}>
            <div className="change_item">
              {item}

              <span onClick={() => removeExchangeItem(item)} />
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
            placeholder={getTranslatedText('addAdv.placeholderChange')}
          />
        </div>
      </div>
      <ErrorDisplay error={!!error && error} />

      <FormikCheckBox
        type="checkbox"
        margin="22px 0 0 0"
        name="readyForOffers"
        value="readyForOffers"
        onChange={readyOffers.setReadyOffer}
        selectedValues={readyOffers.readyOffer}
        text={getTranslatedText('addAdv.readyForOffers')}
      />
    </div>
  );
};

export { Exchange };
