import React, { useContext, useEffect, useState } from 'react';
import { useClickAway } from 'react-use';
import { useSelector } from 'react-redux';
import { animated, useSpring, useTransition } from 'react-spring';

import { categoryImages } from './config';
import { ModalContext } from 'components/common/pop-up';
import { getTranslatedText } from 'components/local/localisation';
import {
  AnimatedLabel,
  DropItems,
  Image,
  PlaceHolder,
  SelectItem,
  SelectLabel,
  SelectTitle,
  WrapSelect,
} from './styles';

const Items = ({
  data,
  showImg,
  refClickAway,
  valueCategory,
  refHeightCategory,
  setItem,
  placeholder,
}) => {
  const { openModal } = useContext(ModalContext);
  const { lang } = useSelector((state) => state.auth);
  const [opened, setOpened] = useState(false);

  useEffect(() => {
    if (opened && !data) {
      openModal({
        title: getTranslatedText('popup.errorTitle', lang),
        children: (
          <p style={{ textAlign: 'center' }}>
            {getTranslatedText('popup.selectCategory', lang)}
          </p>
        ),
      });
    }
  }, [data, opened, lang]);

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
    trail: 100,
    from: { opacity: 0, scale: 0 },
    enter: { opacity: 1, scale: 1, transformOrigin: 5 },
    leave: { opacity: 0, scale: 0 },
    config: { mass: 1, tension: 120, friction: 14 },
  });

  const handleCategory = (item) => {
    setItem(item);
    setOpened((prev) => !prev);
  };

  useClickAway(refClickAway, () => {
    setOpened(false);
  });

  return (
    <WrapSelect ref={refClickAway}>
      <SelectLabel showImg={showImg} onClick={() => setOpened((prev) => !prev)}>
        {valueCategory ? (
          <AnimatedLabel style={spring}>
            {showImg && (
              <Image src={categoryImages[valueCategory]} alt={valueCategory} />
            )}
            <SelectTitle>
              {getTranslatedText(`categories.${valueCategory}`, lang)}
            </SelectTitle>
          </AnimatedLabel>
        ) : (
          <PlaceHolder>{placeholder}</PlaceHolder>
        )}
      </SelectLabel>
      {dropDown(
        (styles, item) =>
          item && (
            <animated.div style={{ ...styles }}>
              <DropItems ref={refHeightCategory} notOpen={opened && !data}>
                {transition((styles, item) => (
                  <animated.div key={item} style={{ ...styles }}>
                    <SelectItem
                      showImg={showImg}
                      onClick={() => handleCategory(item)}
                      selectedItem={valueCategory === item}
                    >
                      {showImg && (
                        <Image src={categoryImages[item]} alt={item} />
                      )}
                      <SelectTitle>
                        {getTranslatedText(`categories.${item}`, lang)}
                      </SelectTitle>
                    </SelectItem>
                  </animated.div>
                ))}
              </DropItems>
            </animated.div>
          )
      )}
    </WrapSelect>
  );
};

export { Items };
