import React from "react";
import mommy from "../../../img/MySettings/mama.png";

import "./active_profile.scss";

const ActiveProfile = () => {
  return (
    <div className="active__profile">
      <div className="active__profile__box">
        <div className="active__profile__box-photo">
          <img src={mommy} alt={"mother"} />
        </div>
        <div className="active__profile__box-data">
          <p className="active__profile__box-data-name">
            Альбина Задорожнaя,
            <span className="active__profile__box-data-city">Харьков</span>
          </p>
        </div>
      </div>
    </div>
  );
};

export default ActiveProfile;
