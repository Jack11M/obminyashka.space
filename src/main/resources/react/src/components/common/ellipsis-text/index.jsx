import { useRef, useEffect, useState } from 'react';

import { useDelay } from 'Utils/delay';

import * as Styles from './styles';

const EllipsisText = ({ children }) => {
  const ref = useRef(null);
  const [heightEl, setHeight] = useState(null);
  const [hasToolTip, setToolTip] = useState(false);
  const [open, setOpen] = useDelay(500);

  const deps = [
    ref?.current?.offsetWidth,
    ref?.current?.scrollWidth,
    ref?.current?.offsetHeight,
    ref?.current?.scrollHeight,
  ];

  useEffect(() => {
    const offsetWidth = ref?.current?.offsetWidth || 0;
    const offsetHeight = ref?.current?.offsetHeight || 0;
    const scrollWidth = ref?.current?.scrollWidth || 0;
    const scrollHeight = ref?.current?.scrollHeight || 0;
    const widthRatio = scrollWidth / offsetWidth;
    const heightRatio = scrollHeight / offsetHeight;

    if (widthRatio > 1.0 || heightRatio > 1.5) {
      setToolTip(true);
    } else {
      setToolTip(false);
    }

    if (ref?.current) {
      const { height } = ref.current.getBoundingClientRect();
      setHeight(height);
    }
  }, [deps]);

  const onMouseEnter = () => {
    if (hasToolTip) {
      setOpen(true);
    }
  };

  const onMouseLeave = () => {
    setOpen(false);
  };

  return (
    <>
      <Styles.EllipsisDiv
        ref={ref}
        onMouseEnter={onMouseEnter}
        onMouseLeave={onMouseLeave}
      >
        {children}
      </Styles.EllipsisDiv>
      {open && (
        <Styles.ToolTip
          height={heightEl}
          onMouseEnter={onMouseEnter}
          onMouseLeave={onMouseLeave}
        >
          {children}
        </Styles.ToolTip>
      )}
    </>
  );
};

export { EllipsisText };
