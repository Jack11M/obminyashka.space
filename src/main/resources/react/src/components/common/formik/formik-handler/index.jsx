import { Formik } from 'formik';

const FormHandler = ({
  onSubmit,
  children,
  initialValues,
  validationSchema,
  ...props
}) => (
  <Formik
    enableReinitialize
    onSubmit={onSubmit}
    validateOnBlur={false}
    validateOnChange={false}
    initialValues={initialValues}
    validationSchema={validationSchema}
    {...props}
  >
    {children}
  </Formik>
);

export { FormHandler };
