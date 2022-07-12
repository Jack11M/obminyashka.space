import { Form, Formik } from 'formik';
// import { useSelector } from 'react-redux';

import { Calendar } from 'components/common';
// import { getProfile } from 'store/profile/slice';
import { getTranslatedText } from 'components/local/localization';

// import InputProfile from '../../../components/inputProfile';
import InputGender from '../../../components/inputProfile/inputGender';

import * as Styles from './styles';

const Children = () => {
  //   const { children } = useSelector(getProfile);

  const child1 = [
    {
      birthDate: '2022-07-10',
      id: 0,
      sex: 'FEMALE',
    },
  ];

  const onSubmit = () => {};

  return (
    <Formik onSubmit={onSubmit} validationSchema={{}} initialValues={{}}>
      {() => (
        <Form>
          <Styles.Block>
            {child1.map((child, idx) => (
              <div key={String(`${idx}_child`)}>
                {/* <InputProfile
                  id={idx}
                  type="date"
                  onChange={null}
                  name="birthDate"
                  value={child.birthDate}
                  label={getTranslatedText('ownInfo.dateOfBirth')}
                /> */}

                <Calendar />

                <InputGender gender={child.sex} id={idx} click={null} />
              </div>
            ))}
          </Styles.Block>

          <Styles.StyledButton
            width="248px"
            disabling={false}
            text={getTranslatedText('button.saveChanges')}
          />
        </Form>
      )}
    </Formik>
  );
};

export { Children };
