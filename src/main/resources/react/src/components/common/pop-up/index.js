import { useEffect, useState, useRef } from 'react';
import { useClickAway } from 'react-use';

import { WrapDiv, ContentModal, DivClose, Title } from './styles';

const Modal = ({ active, setActive, title, children }) => {
  const [open, setOpen] = useState(false);

  useEffect(() => {
    setOpen(true);
  }, [active]);

  const close = () => {
    setOpen(false);
    setTimeout(() => {
      setActive(false);
    }, 1000);
  };

  const ref = useRef(null);
  useClickAway(ref, () => {
    close();
  });
  return (
    <WrapDiv active={open}>
      <ContentModal ref={ref}  active={open}>
        <Title>{title}</Title>
        {children}
        <DivClose onClick={close} />
      </ContentModal>
    </WrapDiv>
  );
};
export { Modal };
