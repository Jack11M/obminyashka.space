import { useEffect } from 'react';
import { useField } from 'formik';
import { useSelector } from 'react-redux';
import { useLocation } from 'react-router-dom';

import { getAuthLang } from 'store/auth/slice';
import { route } from 'routes/routeConstants';

import { InputAuth, InputDiv, Label, SpanError } from './styles';

const InputForAuth = ({ text, ...props }) => {
  const lang = useSelector(getAuthLang);
  const location = useLocation();
  const path = location.pathname === route.login;
  const [field, meta, helpers] = useField(props);
  const { error, touched } = meta;

  useEffect(() => {
    helpers.setError('');
  }, [lang]);

  return (
    <InputDiv path={path}>
      <Label>
        <span>{text}</span>
        <InputAuth error={touched && error} {...field} {...props} />
      </Label>
      <SpanError>{touched && error}</SpanError>
    </InputDiv>
  );
};

export { InputForAuth };
