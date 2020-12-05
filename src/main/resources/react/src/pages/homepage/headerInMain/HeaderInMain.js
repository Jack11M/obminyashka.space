import React from "react";
import furniture from "../../../img/clouds/STEP-one.png";
import monitor from "../../../img/clouds/STEPtwo.png";
import toys from "../../../img/clouds/STEPthree.png";
import "./headerInMain.scss";
import "./moveCloud.scss";

const HeaderInMain = () => {
  return (
    <div className="present-section">
      <div className="wrapper">
        <ul className="presentSlider">
          <li className="presentSlider-element">
            <img src={furniture} alt="furniture" />
            <p className="presentSlider-element__text">
              Накопилось много <b>детских вещей?</b>
            </p>
          </li>

          <li className="presentSlider-element">
            <img src={monitor} alt="monitor" />
            <p className="presentSlider-element__text">
              Просто <b>зарегистрируйся</b> и размести <br />
              объявление!
            </p>
          </li>
          <li className="presentSlider-element ">
            <img src={toys} alt="toys" />
            <p className="presentSlider-element__text">
              <b>Обменяйтесь</b> с другими пользователями <br /> на что-то
              клёвое и <br /> полезное!
            </p>
          </li>
        </ul>
      </div>

      <div className="cloudsInner">
        <span className="cloudOne"/>
        <span className="cloudTwo"/>
        <span className="cloudThree"/>
        <span className="cloudFour"/>
        <span className="cloudFive"/>
        <span className="cloudSix"/>
      </div>
    </div>
  );
};

export default HeaderInMain;
