import { useField } from 'formik';
import InputMask from 'react-input-mask';

import { showPassword } from 'components/common';

import * as Styles from './styles';

const InputProfile = ({ id = '', label, type, ...props }) => {
  const { component, currentType } = showPassword(type !== 'password', 7);
  const [field, meta] = useField(props);
  const { error, touched } = meta;

  return (
    <Styles.ProfileInput>
      <Styles.Label htmlFor={id}>{label}</Styles.Label>

      {field.name.includes('phones') ? (
        <InputMask
          type={type}
          value={field.value}
          id={field.name + id}
          error={touched && error}
          mask="+38(999) 999-99-99"
          onChange={field.onChange}
          readOnly={props.readOnly}
          {...field}
          {...props}
        >
          {(inputProps) => <Styles.Input {...inputProps} disableUnderline />}
        </InputMask>
      ) : (
        <Styles.Input
          type={currentType}
          id={field.name + id}
          error={touched && error}
          readOnly={props.readOnly}
          {...field}
          {...props}
        />
      )}

      {type === 'password' && component}

      <Styles.SpanError> {touched && error}</Styles.SpanError>
    </Styles.ProfileInput>
  );
};

export default InputProfile;
