import React, { useCallback } from 'react';
import "./exit.scss";
import Button from "../../../../components/button/Button";

const Exit = ({ toggle }) => {
  const logOut = useCallback(() => {
    console.log('logOut');
  },[])

  return (
    <div className="modal-overlay">
      <div className="modal">
        <div onClick={toggle} className="modal__cross js-modal-close"/>
        <p className="modal__title">Выйти?</p>
        <p className="modal__text">Вы не сможете оставлять сообщения и добавлять объявления!</p>
        <Button
          whatClass="button"
          text="Выход"
          width={'179px'}
          click={logOut}
        />
        <div className={'background'}>
          <img src={require("../../../../img/log-out-2.png")} className="log-out-img" alt="log-out"/>
        </div>
      </div>
    </div>
  )
}
export default Exit;
