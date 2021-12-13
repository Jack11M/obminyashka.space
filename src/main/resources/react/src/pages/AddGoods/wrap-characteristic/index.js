import React, { useEffect } from 'react';
import { useField } from 'formik';
import { useSelector } from 'react-redux';

import { H4 } from './styles';

const WrapCharacteristic = ({ title, name, children }) => {
  const { lang } = useSelector((state) => state.auth);
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
