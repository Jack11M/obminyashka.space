import { useField } from 'formik';
import { useLocation } from 'react-router-dom';

import { route } from 'routes/routeConstants';

import { InputAuth, InputDiv, Label, SpanError } from './styles';

const InputForAuth = ({ text, ...props }) => {
  const location = useLocation();
  const path = location.pathname === route.login;
  const [field, meta] = useField(props);
  const { error, touched } = meta;

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
