import React from "react";
import BtnGoodBusiness from "../../../assets/btnGoodBusiness/BtnGoodBusiness.jsx";
import "./helpchildren.scss";

const HelpChildren = () => {
  return (
    <div className="HelpChildren">
      <h3 className="HelpChildren__title">Чужих детей не бывает!</h3>
      <p className="HelpChildren__text">
        <strong>Обменяшка</strong> сотрудничает с волонтерскими организациями по
        всей Украине! Ты тоже можешь помочь! Отдай свои ненужные вещи, они
        попадут в детские дома и приюты!
      </p>
      <BtnGoodBusiness
        whatClass={"HelpChildren__btn "}
        text={"я хочу помочь детям!"}
        href={"#"}
      />
    </div>
  );
};

export default HelpChildren;
