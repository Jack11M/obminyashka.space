import React from "react";

import "./active_profile.scss";
import Avatar from '../../../components/common/avatar/avatar';

const ActiveProfile = ({firstName, lastName, avatar}) => {
  return (
    <div className="active__profile">
      <div className="active__profile__box">
        <Avatar whatIsClass={"active__profile__box-photo"} width={135} height={135} avatar={avatar}/>
        <div className="active__profile__box-data">
          <p className="active__profile__box-data-name">
            {`${firstName} ${lastName}`}
          </p>
        </div>
      </div>
    </div>
  );
};

export default ActiveProfile;
