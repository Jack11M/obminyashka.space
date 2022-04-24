import React, { useEffect } from 'react';
import { useField } from 'formik';
import styled from 'styled-components';
import InputMask from 'react-input-mask';
import { useSelector } from 'react-redux';

import { getLang } from 'store/auth/slice';

const ProfileInput = styled.div`
  position: relative;
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 21px;
`;

const Label = styled.label`
  font-size: 14px;
  line-height: 16px;
  display: inline-flex;
  cursor: pointer;
`;

const Input = styled.input`
  display: inline-flex;
  box-sizing: border-box;
  width: 415px;
  padding: 9px 16px 9px 16px;
  border: 1px solid #bcbcbc;
  border-radius: 2px;
  outline: none;
  font-size: 16px;
  line-height: 16px;
  font-family: inherit;
  border: 1px solid
    ${({ theme: { colors }, error }) =>
      error ? colors['colorError'] : 'hsl(0, 0%, 74%)'};
  color: ${({ theme: { colors } }) => colors['right-color-text']};

  &::placeholder {
    color: #a3a3a3;
  }
  &:focus,
  &:hover {
    border-color: ${({ theme: { colors }, error }) =>
      error ? colors['colorError'] : 'hsl(0, 0%, 44%)'};
  }
`;

const SpanError = styled.span`
  position: absolute;
  bottom: -17px;
  left: 135px;
  font-size: 11px;
  font-style: normal;
  font-weight: 400;
  line-height: 20px;
  color: ${({ theme: { colors } }) => colors['colorError']};
`;

const InputProfile = ({ id = '', label, ...props }) => {
  const lang = useSelector(getLang);
  const [field, meta, helpers] = useField(props);
  const { error, touched } = meta;

  useEffect(() => {
    helpers.setError('');
  }, [lang]);

  return (
    <ProfileInput>
      <Label htmlFor={id}>{`${label}`}</Label>

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
          {(inputProps) => <Input {...inputProps} disableUnderline />}
        </InputMask>
      ) : (
        <Input
          readOnly={props.readOnly}
          id={field.name + id}
          {...field}
          {...props}
          error={touched && error}
        />
      )}
      {<SpanError> {touched && error}</SpanError>}
    </ProfileInput>
  );
};

export default InputProfile;
