import { useField } from 'formik';

import { ItemTittle } from '../styles';

const WrapCharacteristic = ({ title, name, children }) => {
  const [, meta] = useField({ name });
  const { error } = meta;

  return (
    <>
      <ItemTittle error={error} name={name}>
        {title}:
      </ItemTittle>
      {children}
    </>
  );
};

export { WrapCharacteristic };
