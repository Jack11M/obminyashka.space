import { useField } from 'formik';

import { H4 } from './styles';

const WrapCharacteristic = ({ title, name, children }) => {
  const [, meta] = useField({ name });
  const { error } = meta;

  return (
    <>
      <H4 error={error}>{title}:</H4>
      {children}
    </>
  );
};

export { WrapCharacteristic };
