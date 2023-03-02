import { Icon } from 'obminyashka-components';
import { useNavigate } from 'react-router-dom';

import * as Styles from './styles';

const BackButton = ({ onClick, icon, text, ...props }) => {
  const navigate = useNavigate();

  return (
    <Styles.Button onClick={onClick || (() => navigate(-1))} {...props}>
      <Styles.Icon>{icon || <Icon.ChevronLeft />}</Styles.Icon>
      {text && <Styles.Text>{text}</Styles.Text>}
    </Styles.Button>
  );
};

export { BackButton };
