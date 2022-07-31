import { memo, useEffect } from 'react';
import { useField } from 'formik';
import { useSelector } from 'react-redux';
import { useLocation } from 'react-router-dom';

import { getLang } from 'store/auth/slice';
import { route } from 'routes/routeConstants';

import * as Styles from './styles';

const InputForAuth = ({ text, ...props }) => {
  const lang = useSelector(getLang);
  const location = useLocation();
  const path = location.pathname === route.login;
  const [field, meta, helpers] = useField(props);
  const { error, touched } = meta;

  useEffect(() => {
    helpers.setError('');
  }, [lang]);

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

export default memo(InputForAuth);
