import { useSelector } from 'react-redux';
import { FieldArray, Form, Formik } from 'formik';

import { getProfile } from 'store/profile/slice';
import { getTranslatedText } from 'components/local/localization';

import { Gender } from './gender';
import * as Styles from './styles';
import { Calendar } from './calendar';
import { getInitialValues, validationSchema } from './config';

const Children = () => {
  const { children } = useSelector(getProfile);

  const initialValues = getInitialValues(children);

  const onSubmit = (values) => {
    console.log('submit ', values);
  };

  return (
    <Formik
      onSubmit={onSubmit}
      initialValues={initialValues}
      validationSchema={validationSchema}
    >
      {({ values }) => (
        <Form>
          <FieldArray name="children">
            {() => {
              return (
                <Styles.Block>
                  {values.children.map((child, idx) => (
                    <div key={child.id}>
                      <Calendar name={`children.${idx}.birthDate`} />
                      <Gender name={`children.${idx}.sex`} />
                    </div>
                  ))}
                </Styles.Block>
              );
            }}
          </FieldArray>

          <Styles.StyledButton
            type="submit"
            width="248px"
            text={getTranslatedText('button.saveChanges')}
          />
        </Form>
      )}
    </Formik>
  );
};

export { Children };
