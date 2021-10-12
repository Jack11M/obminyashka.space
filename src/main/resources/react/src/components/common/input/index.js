import React, { memo, useEffect } from 'react';
import { useLocation } from 'react-router-dom';
import { useField } from 'formik';
import { useSelector } from 'react-redux';

import { route } from 'routes/routeConstants';

import { InputAuth, InputDiv, Label, SpanError } from './styles';

const InputForAuth = ({ text, ...props }) => {
  const { lang } = useSelector((state) => state.auth);
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
        {text}
        <InputAuth error={touched && error} {...field} {...props} />
      </Label>
      <SpanError>{touched && error}</SpanError>
    </InputDiv>
  );
};

export default memo(InputForAuth);
