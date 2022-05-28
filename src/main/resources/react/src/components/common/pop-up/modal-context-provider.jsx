import { useState, useMemo } from 'react';
import { ModalContext } from './modal-context';
import { Modal } from './modal';

export const ModalProvider = ({ children }) => {
  const [modalOpened, setModalOpened] = useState(false);
  const [modalContent, setModalContent] = useState(null);

  const openModal = (modalConfig) => {
    setModalOpened(true);
    setModalContent(modalConfig);
  };

  const closeModal = () => {
    setModalOpened(false);
  };

  const valueModalProvider = useMemo(() => ({
    openModal,
    closeModal,
  }), []);

  return (
    <ModalContext.Provider value={valueModalProvider}>
      {modalOpened && <Modal {...modalContent} />}
      {children}
    </ModalContext.Provider>
  );
};
