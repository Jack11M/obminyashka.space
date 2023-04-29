import { Formik } from 'formik';

const FormikHandler = ({
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

export { FormikHandler };
