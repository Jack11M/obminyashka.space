import React from 'react';
import { NavLink } from 'react-router-dom';
import { useSelector } from 'react-redux';

import { getTranslatedText } from 'components/local/localisation';

import { links } from './config';

import './tabs.scss';

const Tabs = ({ toggle }) => {
  const { lang } = useSelector((state) => state.auth);

  return (
    <div className="tabs">
      {links.map(({ url, onClick, end, classname, textKey }, index) => (
        <NavLink
          to={url}
          end={end}
          key={String(index)}
          onClick={onClick ? toggle : undefined}
        >
          <i className={classname} />
          {getTranslatedText(textKey, lang)}
          <i className="active__cycle" />
        </NavLink>
      ))}
    </div>
  );
};

export { Tabs };
