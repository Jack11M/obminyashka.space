import { Link } from 'react-router-dom';

import { route } from 'routes/routeConstants';
import { getTranslatedText } from 'components/local/localization';

import './buttonAdv.scss';

const ButtonAdv = ({ type = 'submit' }) => (
  <>
    {type === 'link' && (
      <Link to={route.addAdv} className="btn-adv">
        <span>{getTranslatedText('button.addAdv')}</span>
      </Link>
    )}

    {type === 'submit' && (
      <button type="submit" className="btn-adv">
        <span>{getTranslatedText('button.addAdv')}</span>
      </button>
    )}
  </>
);

export { ButtonAdv };
