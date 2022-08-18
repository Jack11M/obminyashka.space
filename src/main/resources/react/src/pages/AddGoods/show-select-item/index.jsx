import { useState, useRef, useEffect, useContext } from 'react';
import { useField } from 'formik';
import { animated, useSpring, useTransition } from 'react-spring';

import { ModalContext } from 'components/common';
import { useOutsideClick } from 'hooks/useOutsideClick';
import { getTranslatedText } from 'components/local/localization';

import * as Styles from './styles';
import { categoryImages } from './config';
import { ErrorDisplay } from '../error-display';

const ShowSelectItem = ({
  name,
  data,
  text,
  value,
  onClick,
  typeError,
  titleError,
  placeholder,
  showImg = false,
  overflows = false,
  categories = false,
}) => {
  const { openModal } = useContext(ModalContext);
  const [opened, setOpened] = useState(false);

  const [, meta] = useField(name);
  const { error } = meta;

  useEffect(() => {
    if (opened && !data) {
      setOpened(false);
      openModal({
        title: getTranslatedText(titleError),
        children: (
          <p style={{ textAlign: 'center' }}>{getTranslatedText(typeError)}</p>
        ),
      });
    }
  }, [data, opened, openModal, typeError]);

  const refClickAway = useRef();
  const refHeightCategory = useRef();

  const spring = useSpring({
    opacity: !opened ? 1 : 0,
    scale: !opened ? 1 : 0,
    transformOrigin: !opened ? 5 : 0,
  });

  const dropDown = useTransition(opened, {
    from: { height: 0 },
    enter: { height: refHeightCategory?.current?.offsetHeight },
    leave: { height: 0 },
  });

  const transition = useTransition(opened ? data : [], {
    trail: 75,
    from: { opacity: 0, scale: 0 },
    enter: { opacity: 1, scale: 1, transformOrigin: 5 },
    leave: { opacity: 0, scale: 0 },
    config: { mass: 1, tension: 120, friction: 14 },
  });

  const handleClick = (item) => {
    onClick(item);
    setOpened((prev) => !prev);
  };

  useOutsideClick(refClickAway, () => {
    setOpened(false);
  });

  return (
    <>
      <Styles.WrapSelect ref={refClickAway}>
        <Styles.SelectLabel
          error={!!error}
          showImg={showImg}
          onClick={() => setOpened((prev) => !prev)}
        >
          {value ? (
            <Styles.AnimatedLabel style={spring}>
              {showImg && (
                <Styles.Image src={categoryImages[value]} alt={value} />
              )}
              <Styles.SelectTitle>{text}</Styles.SelectTitle>
            </Styles.AnimatedLabel>
          ) : (
            <Styles.PlaceHolder>{placeholder}</Styles.PlaceHolder>
          )}
        </Styles.SelectLabel>

        {dropDown(
          (styles, items) =>
            items && (
              <animated.div style={{ ...styles }}>
                <Styles.DropItems
                  overflows={overflows}
                  ref={refHeightCategory}
                  notOpen={opened && !data}
                >
                  {transition((style, item) => (
                    <animated.div key={item} style={{ ...style }}>
                      <Styles.SelectItem
                        showImg={showImg}
                        onClick={() => handleClick(item)}
                        selectedItem={value === item?.name ?? item}
                      >
                        {showImg && (
                          <Styles.Image
                            src={categoryImages[item.name]}
                            alt={item.name}
                          />
                        )}

                        <Styles.SelectTitle>
                          {categories &&
                            getTranslatedText(`categories.${item?.name}`)}

                          {!categories && item}
                        </Styles.SelectTitle>
                      </Styles.SelectItem>
                    </animated.div>
                  ))}
                </Styles.DropItems>
              </animated.div>
            )
        )}
      </Styles.WrapSelect>

      <ErrorDisplay error={!!error && error} />
    </>
  );
};

export { ShowSelectItem };
