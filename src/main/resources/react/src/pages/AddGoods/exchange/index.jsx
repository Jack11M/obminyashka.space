import { useState } from 'react';
import { useField } from 'formik';
import { useTransition, animated } from 'react-spring';

import { getTranslatedText } from 'components/local/localization';

import { ErrorDisplay } from '../error-display';

import * as Styles from '../styles';

const Exchange = ({ exchangeList, setExchange }) => {
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
    <Styles.Wrap>
      <Styles.TitleH3>
        {getTranslatedText('addAdv.exchange')}{' '}
        <span className="span_star">*</span>
      </Styles.TitleH3>

      <Styles.Description>
        &nbsp;
        {getTranslatedText('addAdv.whatChange')}
      </Styles.Description>

      <Styles.Explanation>
        ({getTranslatedText('addAdv.enterPhrase')})
      </Styles.Explanation>

      <Styles.ChangeWrapp
        styles={getBorderClassName}
        borderValue={border}
        errorValue={error}
      >
        {transitions((styles, item) => (
          <animated.div key={item} style={{ ...styles }}>
            <Styles.ChangeItem>
              {item}

              <Styles.Span onClick={() => removeExchangeItem(item)} />
            </Styles.ChangeItem>
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
      </Styles.ChangeWrapp>
      <ErrorDisplay error={!!error && error} />
    </Styles.Wrap>
  );
};

export { Exchange };
