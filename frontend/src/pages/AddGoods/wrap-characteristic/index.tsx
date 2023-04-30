/* eslint-disable */
// @ts-nocheck
// TODO: fix typescript
import { useField } from 'formik';

import * as Styles from './styles';
import { ItemTittle } from '../styles';

const WrapCharacteristic = ({ title, name, children }) => {
  const [, meta] = useField({ name });
  const { error } = meta;

  return (
    <>
      <ItemTittle error={error} name={name}>
        {title}:
      </ItemTittle>

      <Styles.Div>{children}</Styles.Div>
    </>
  );
};

export { WrapCharacteristic };
