import { useEffect, useState } from 'react';
import { useField } from 'formik';
import { useTransition, animated } from 'react-spring';
import { Modal, Subtitle } from 'obminyashka-components';

import { FormikCheckBox } from 'components/common/formik';
import { ErrorDisplay } from 'pages/AddGoods/error-display';
import { getTranslatedText } from 'components/local/localization';

import * as Styles from './styles';

const Exchange = ({ exchangeList, setExchange, readyOffers }) => {
  const [border, setBorder] = useState(false);
  const [isOpenModal, setIsOpenModal] = useState(false);
  const [exchangeInput, setExchangeInput] = useState('');
  const [exchangeInputError, setExchangeInputError] = useState('');

  const [, meta, helpers] = useField({ name: 'wishesToExchange' });
  const { error } = meta;

  const transitions = useTransition(exchangeList.length ? exchangeList : [], {
    from: { opacity: 0, scale: 0 },
    enter: { opacity: 1, scale: 1 },
    leave: { opacity: 0, scale: 0 },
    config: { mass: 1, tension: 120, friction: 14, duration: 200 },
  });

  useEffect(() => {
    if (!isOpenModal) setExchangeInputError('');
  }, [isOpenModal]);

  const exchangeUnique = (arr, item) =>
    !arr.filter((el) => el.toLowerCase() === item.toLowerCase()).length;

  const validate = (inputValue) => {
    const totalLength = exchangeList.join('').length;
    const inputLength = inputValue?.length;
    const minSizeInput = inputValue.replaceAll(' ', '').length < 3;

    if (minSizeInput) {
      helpers.setError(getTranslatedText('errors.min3'));
    }

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
    const minSizeInput = exchangeInput.replaceAll(' ', '').length < 3;

    if (!exchangeInput) {
      if (event.key === 'Enter') event.preventDefault();
      return;
    }

    if (event.key === 'Enter') {
      event.preventDefault();

      if (minSizeInput) return;

      setExchange((prev) => {
        if (!exchangeUnique(prev, exchangeInput)) {
          setExchangeInputError(exchangeInput);
          setIsOpenModal(true);
          return [...prev];
        }

        return [...prev, exchangeInput];
      });

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
      setExchange((prev) => {
        if (!exchangeUnique(prev, exchangeInput)) {
          setExchangeInputError(exchangeInput);
          setIsOpenModal(true);
          return [...prev];
        }

        return [...prev, exchangeInput];
      });
      setExchangeInput('');
    }

    setBorder(false);
  };

  return (
    <>
      <Styles.Wrap name="wishesToExchange">
        <Subtitle textTitle={getTranslatedText('addAdv.exchange')} />

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

        <FormikCheckBox
          type="checkbox"
          name="readyForOffers"
          value="readyForOffers"
          style={{ marginTop: 20 }}
          onChange={readyOffers.setReadyOffer}
          selectedValues={readyOffers.readyOffer}
          text={getTranslatedText('addAdv.readyForOffers')}
        />

        <ErrorDisplay error={!!error && error} marginTop="10px" />
      </Styles.Wrap>

      <Modal isOpen={isOpenModal} onClose={setIsOpenModal}>
        <h3 style={{ textAlign: 'center', marginBottom: 10 }}>
          {exchangeInputError}
        </h3>
        <p>{getTranslatedText('popup.addedExchange')}</p>
      </Modal>
    </>
  );
};

export { Exchange };
