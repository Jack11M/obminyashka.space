import React from "react";
import "./errorPage.scss";
import {
  loop,
  fourOhFour,
  shadow,
  shadowDark,
  orangeDots,
  greenDots
} from "../../img/all_images_export/errorPage";
import Button from "../../components/button/Button";

const ErrorPage = ({ goTo }) => {
  return (
    <div className={"error-page"}>
      <div className={"blockCenterImage"}>
        <div className={"blockCenterImage-fourOhfour"}>
          <img src={fourOhFour} alt={"404"} />
        </div>
        <div className={"blockCenterImage-shadow"}>
          <img
            className={"blockCenterImage-shadow_light"}
            src={shadow}
            alt={"shadow"}
          />
          <img
            className={"blockCenterImage-shadow_dark"}
            src={shadowDark}
            alt={"shadow dark"}
          />
        </div>
      </div>
      <div className={"blockOrangeImage"}>
        <img src={orangeDots} alt={"orange dots"} />
      </div>
      <div className={"blockGreenImage"}>
        <img src={greenDots} alt={"green dots"} />
      </div>
      <div className={"blockRightImage"}>
        <img src={loop} alt={"loop"} />
      </div>
      <h2>Страница не существует!</h2>
      <div className={"blockButtons"}>
        <Button whatClass={"onMain"} text={"На главную"} click={goTo} />
        <Button whatClass={"back"} text={"Назад"} click={goTo} />
      </div>
    </div>
  );
};
export default ErrorPage;
