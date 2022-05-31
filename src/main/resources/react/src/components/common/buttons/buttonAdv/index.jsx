import { useSelector } from 'react-redux';
import { Link } from 'react-router-dom';

import { getLang } from 'store/auth/slice';
import { route } from 'routes/routeConstants';
import { getTranslatedText } from 'components/local/localization';

import './buttonAdv.scss';

const ButtonAdv = ({ type = 'submit' }) => {
  const lang = useSelector(getLang);
  return (
    <>
      {type === 'link' && (
        <Link to={route.addAdv} className="btn-adv">
          <span>{getTranslatedText('button.addAdv', lang)}</span>
        </Link>
      )}

      {type === 'submit' && (
        <button type="submit" className="btn-adv">
          <span>{getTranslatedText('button.addAdv', lang)}</span>
        </button>
      )}
    </>
  );
};

export { ButtonAdv };
