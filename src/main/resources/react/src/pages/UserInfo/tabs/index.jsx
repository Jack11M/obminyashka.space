import { NavLink } from 'react-router-dom';
import { useSelector } from 'react-redux';

import { getLang } from 'store/auth/slice';
import { getTranslatedText } from 'components/local/localization';

import { links } from './config';

import './tabs.scss';

const Tabs = ({ toggle }) => {
  const lang = useSelector(getLang);

  return (
    <div className="tabs">
      {links.map(({ url, onClick, end, classname, textKey }) => (
        <NavLink
          to={url}
          end={end}
          key={url}
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
