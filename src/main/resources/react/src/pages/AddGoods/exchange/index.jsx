import { useState } from 'react';
import { useField } from 'formik';
import { useTransition, animated } from 'react-spring';

import { FormikCheckBox } from 'components/common/formik';
import { ErrorDisplay } from 'pages/AddGoods/error-display';
import { getTranslatedText } from 'components/local/localization';

import * as Styles from './styles';

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

  const validate = (inputValue) => {
    const totalLength = exchangeList.join('').length;
    const inputLength = inputValue?.length;

    if (inputLength > 40) {
      helpers.setError(getTranslatedText('errors.max40'));
      return true;
    }

    if (totalLength + inputLength > 200) {
      helpers.setError(getTranslatedText('errors.max200'));
      return true;
    }
    return false;
  };

  const handleInput = (event) => {
    helpers.setError('');
    if (validate(event.target.value)) {
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
    if (exchangeInput && !error) {
      setExchange((prev) => [...prev, exchangeInput]);
      setExchangeInput('');
    }

    setBorder(false);
  };

  return (
    <Styles.Wrap name="wishesToExchange">
      <Styles.TitleH3>{getTranslatedText('addAdv.exchange')}</Styles.TitleH3>

      <Styles.Description>
        &nbsp;
        {getTranslatedText('addAdv.whatChange')}
      </Styles.Description>

      <Styles.Explanation>
        ({getTranslatedText('addAdv.enterPhrase')})
      </Styles.Explanation>

      <Styles.ChangeWrap borderValue={border} error={error}>
        {transitions((styles, item, idx) => {
          return (
            <animated.div key={idx.ctrl.id} style={{ ...styles }}>
              <Styles.ChangeItem>
                {item}
                <Styles.Span onClick={() => removeExchangeItem(item)} />
              </Styles.ChangeItem>
            </animated.div>
          );
        })}

        <Styles.ChangeInput
          type="text"
          onBlur={onBlur}
          onFocus={onFocus}
          value={exchangeInput}
          onKeyPress={keyEnter}
          onChange={handleInput}
          placeholder={getTranslatedText('addAdv.placeholderChange')}
        />
      </Styles.ChangeWrap>

      <ErrorDisplay error={!!error && error} />

      <FormikCheckBox
        type="checkbox"
        name="readyForOffers"
        value="readyForOffers"
        style={{ marginTop: 20 }}
        onChange={readyOffers.setReadyOffer}
        selectedValues={readyOffers.readyOffer}
        text={getTranslatedText('addAdv.readyForOffers')}
      />
    </Styles.Wrap>
  );
};

export { Exchange };
