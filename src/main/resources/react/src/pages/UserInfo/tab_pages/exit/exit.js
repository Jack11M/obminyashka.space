import React from 'react';
import "./exit.scss";
import Button from "../../../../components/button/Button";
import {useDispatch, useSelector} from "react-redux";
import {checkModal, toggleModal} from "../../../../redux/profile/profileAction";

const Exit = () => {
  const dispatch = useDispatch();
  const {modalIsOpening} = useSelector(state => state.profileMe);

  const toggle = () => {
    dispatch(toggleModal());
    dispatch(checkModal(1));
  }

  const logOut = () => {
    console.log('logOut');
  }

  return (
    <div className="modal-overlay">
      <div className="modal">
        <svg onClick={() => toggle()} className="modal__cross js-modal-close" width="12" height="12"
             viewBox="-9 -9 32 32"
             fill="none"
             xmlns="http://www.w3.org/2000/svg">
          <path d="M1.86253 13.1315L0.868164 12.1371L12.1371 0.86821L13.1315 1.86257L1.86253 13.1315Z" fill="white"/>
          <path d="M12.1371 13.1312L0.868164 1.86228L1.86252 0.867922L13.1314 12.1368L12.1371 13.1312Z" fill="white"/>
        </svg>
        <p className="modal__title">Выйти?</p>
        <p className="modal__text">Вы не сможете оставлять сообщения и добавлять объявления!</p>
        <Button
          whatClass="button"
          text="Выход"
          width={'179px'}
          click={logOut}
        />
        <div>
          <img src={require("./../../../../img/logout.png")} alt="log-out"/>
          <img src={require("../../../../img/log-out-2.png")} className="log-out-img" alt="log-out"/>
        </div>
      </div>
    </div>
  )
}
export default Exit;
