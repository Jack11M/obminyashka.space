import { useField } from 'formik';
import { useLocation } from 'react-router-dom';

import { route } from 'routes/routeConstants';

import * as Styles from './styles';

const InputForAuth = ({ text, ...props }) => {
  const location = useLocation();
  const path = location.pathname === route.login;
  const [field, meta] = useField(props);
  const { error, touched } = meta;

  return (
    <Styles.InputDiv path={path}>
      <Styles.Label>
        {text}
        <Styles.InputAuth error={touched && error} {...field} {...props} />
      </Styles.Label>
      <Styles.SpanError>{touched && error}</Styles.SpanError>
    </Styles.InputDiv>
  );
};

export { InputForAuth };
