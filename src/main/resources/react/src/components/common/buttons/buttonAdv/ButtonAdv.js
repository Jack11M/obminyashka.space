import React from 'react';
import { useSelector } from 'react-redux';
import { Link } from 'react-router-dom';

import { route } from 'routes/routeConstants';
import { getTranslatedText } from 'components/local/localization';

import './buttonAdv.scss';

const ButtonAdv = ({ type }) => {
  const { lang } = useSelector((state) => state.auth);
  return (
    <>
      {type === 'link' && (
        <Link to={route.addAdv} className="btn-adv">
          <span>{getTranslatedText('button.addAdv', lang)}</span>
        </Link>
      )}

      {type === 'submit' && (
        <button type={type} className="btn-adv">
          <span>{getTranslatedText('button.addAdv', lang)}</span>
        </button>
      )}
    </>
  );
};

export default ButtonAdv;
