import React from "react";
import { Link } from 'react-router-dom';
import "./buttonAdv.scss";

const ButtonAdv = () => {
  return (
    <Link to={'/'} className="btn-adv">
      <span>Добавить объявление</span>
    </Link>
  );
};

export default ButtonAdv;
