import React from 'react';

import './active_profile.scss';
import { Avatar } from 'components/common/avatar';

const ActiveProfile = ({ firstName, lastName, avatar }) => {
  return (
    <div className="active__profile">
      <div className="active__profile__box">
        <Avatar
          width={135}
          height={135}
          avatar={avatar}
          whatIsClass="active__profile__box-photo"
        />
        <div className="active__profile__box-data">
          <p className="active__profile__box-data-name">
            {`${firstName} ${lastName}`}
          </p>
        </div>
      </div>
    </div>
  );
};

export { ActiveProfile };
