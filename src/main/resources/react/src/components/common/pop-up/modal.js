import { useState, useRef, useContext } from 'react';
import { useClickAway } from 'react-use';
import { ModalContext } from './modal-context';

import { WrapDiv, ContentModal, DivClose, Title, Content } from './styles';

const Modal = ({ title, children }) => {
  const { closeModal } = useContext(ModalContext);
  const [closing, setClosing] = useState(false);
  const DELAY = 275;

  const handleClose = () => {
    setClosing(true);
    const closeTimout = setTimeout(() => {
      setClosing(false);
      closeModal();
      clearTimeout(closeTimout);
    }, DELAY);
  };

  const ref = useRef(null);
  useClickAway(ref, () => {
    handleClose();
  });
  return (
    <WrapDiv delay={DELAY} closing={closing}>
      <ContentModal ref={ref} delay={DELAY} closing={closing}>
        <Title>{title}</Title>
        <Content>{children}</Content>
        <DivClose onClick={handleClose} />
      </ContentModal>
    </WrapDiv>
  );
};
export { Modal };
