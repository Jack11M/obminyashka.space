import React, { useEffect } from 'react';
import { useField } from 'formik';
import { useSelector } from 'react-redux';

import { getLang } from 'store/auth/slice';

import { H4 } from './styles';

const WrapCharacteristic = ({ title, name, children }) => {
  const lang = useSelector(getLang);
  const [, meta, helpers] = useField({ name });
  const { error } = meta;

  useEffect(() => {
    helpers.setError(undefined);
  }, [lang]);

  return (
    <>
      <H4 error={error}>{title}:</H4>
      {children}
    </>
  );
};

export { WrapCharacteristic };
