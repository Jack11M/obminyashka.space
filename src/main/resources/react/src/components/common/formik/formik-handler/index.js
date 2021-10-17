import React from 'react';
import { Formik } from 'formik';

const FormHandler = ({
  onSubmit,
  initialValues,
  validationSchema,
  ...props
}) => {
  return (
    <Formik
      enableReinitialize
      onSubmit={onSubmit}
      validateOnBlur={false}
      validateOnChange={false}
      initialValues={initialValues}
      validationSchema={validationSchema}
      {...props}
    >
      {() => <>{React.cloneElement(props.children)}</>}
    </Formik>
  );
};

export { FormHandler };
