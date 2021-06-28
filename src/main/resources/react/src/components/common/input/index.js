import React, { memo } from 'react';
import { useLocation } from 'react-router-dom';

import { route } from '../../../routes/routeConstants';

import { InputAuth, InputDiv, Label, SpanError } from './styles';
import { useField } from 'formik';

const InputForAuth = ({ text, ...props }) => {
  const location = useLocation();
  const path = location.pathname === route.login;
  const [field, meta] = useField(props);
  const { error, touched } = meta;

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
