/* eslint-disable react/jsx-no-useless-fragment */
import { useState } from 'react';
import { useField } from 'formik';
import { useTransition, animated } from 'react-spring';

import { FormikCheckBox } from 'components/common/formik';
// import { ErrorDisplay } from 'pages/AddGoods/error-display';
import { getTranslatedText } from 'components/local/localization';

import * as Styles from './styles';

const Exchange = ({ exchangeList, setExchange, readyOffers }) => {
  const [exchangeInput, setExchangeInput] = useState('');
  const [border, setBorder] = useState(false);

  const [, helpers] = useField({ name: 'wishesToExchange' });

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
      <Styles.TitleH3>{getTranslatedText('addAdv.exchange')}</Styles.TitleH3>

      <Styles.Description>
        &nbsp;
        {getTranslatedText('addAdv.whatChange')}
      </Styles.Description>

      <Styles.Explanation>
        ({getTranslatedText('addAdv.enterPhrase')})
      </Styles.Explanation>

      <Styles.ChangeWrapp borderValue={border} styles={getBorderClassName}>
        {transitions((styles, item) => (
          <animated.div key={item} style={{ ...styles }}>
            <Styles.ChangeItem>
              {item}
              <Styles.Span onClick={() => removeExchangeItem(item)} />
            </Styles.ChangeItem>
          </animated.div>
        ))}
        <>
          <Styles.ChangeInput
            type="text"
            onBlur={onBlur}
            onFocus={onFocus}
            value={exchangeInput}
            onKeyPress={keyEnter}
            onChange={handleInput}
            placeholder={getTranslatedText('addAdv.placeholderChange')}
          />
        </>
      </Styles.ChangeWrapp>

      <FormikCheckBox
        type="checkbox"
        margin="22px 0 0 0"
        name="readyForOffers"
        value="readyForOffers"
        onChange={readyOffers.setReadyOffer}
        selectedValues={readyOffers.readyOffer}
        text={getTranslatedText('addAdv.readyForOffers')}
      />
    </Styles.Wrap>
  );
};

export { Exchange };
