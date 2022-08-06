import { useField } from 'formik';
import InputMask from 'react-input-mask';

import * as Styles from './styles';

const InputProfile = ({ id = '', label, ...props }) => {
  const [field, meta] = useField(props);
  const { error, touched } = meta;

  return (
    <Styles.ProfileInput>
      <Styles.Label htmlFor={id}>{label}</Styles.Label>

      {field.name.includes('phones') ? (
        <InputMask
          mask="+38(999) 999-99-99"
          value={field.value}
          onChange={field.onChange}
          readOnly={props.readOnly}
          id={field.name + id}
          {...field}
          {...props}
          error={touched && error}
        >
          {(inputProps) => <Styles.Input {...inputProps} disableUnderline />}
        </InputMask>
      ) : (
        <Styles.Input
          readOnly={props.readOnly}
          id={field.name + id}
          {...field}
          {...props}
          error={touched && error}
        />
      )}
      <Styles.SpanError> {touched && error}</Styles.SpanError>
    </Styles.ProfileInput>
  );
};

export default InputProfile;
