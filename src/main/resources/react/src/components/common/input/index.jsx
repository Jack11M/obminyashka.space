import { useField } from 'formik';
import { useLocation } from 'react-router-dom';

import { route } from 'routes/routeConstants';
import { showPassword } from 'components/common';

import * as Styles from './styles';

const InputForAuth = ({ text, type, ...props }) => {
  const location = useLocation();
  const notPasswordType = type !== 'password';
  const path = location.pathname === route.login;

  const { component, currentType } = showPassword(notPasswordType);
  const typing = notPasswordType ? type : currentType;

  const [field, meta] = useField(props);
  const { error, touched } = meta;

  return (
    <Styles.InputDiv path={path}>
      <Styles.Label>
        {text}

        <Styles.InputAuth
          type={typing}
          error={touched && error}
          {...field}
          {...props}
        />
      </Styles.Label>

      {!notPasswordType && component}
      <Styles.SpanError>{touched && error}</Styles.SpanError>
    </Styles.InputDiv>
  );
};

export { InputForAuth };
