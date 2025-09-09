import { IModal } from "./types";

import * as Styles from "./styles";

export const Modal = ({ isModal, setIsModal, children }: IModal) => {
  return (
    <Styles.ModalWrapper isModal={isModal} onClick={() => setIsModal(false)}>
      <Styles.ModalContent
        isModal={isModal}
        onClick={(e) => e.stopPropagation()}
      >
        {children}
        <Styles.Cross onClick={() => setIsModal(false)} />
      </Styles.ModalContent>
    </Styles.ModalWrapper>
  );
};
