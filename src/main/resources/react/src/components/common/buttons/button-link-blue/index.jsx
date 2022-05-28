import { Link } from 'react-router-dom';

const ButtonLinkBlue = ({ href, whatClass, text }) => (
  <Link to={href} className={whatClass}>
    {text}
  </Link>
);

export { ButtonLinkBlue };
