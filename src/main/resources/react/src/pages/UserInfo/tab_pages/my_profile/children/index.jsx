import { useMemo } from 'react';
import { toast } from 'react-toastify';
import { FieldArray, Form, Formik } from 'formik';
import { useSelector, useDispatch } from 'react-redux';

import { enumSex } from 'config/ENUM';
import { Button } from 'components/common';
import { getProfile } from 'store/profile/slice';
import { postChildrenThunk } from 'store/profile/thunk';
import { getTranslatedText } from 'components/local/localization';
import ButtonsAddRemoveChild from 'pages/UserInfo/components/buttonsAddRemoveChild';

import { Gender } from './gender';
import { Calendar } from './calendar';
import { getInitialValues, validationSchema } from './config';

const amount = 10;

const Children = () => {
  const dispatch = useDispatch();
  const { children } = useSelector(getProfile);

  const initialValues = useMemo(() => getInitialValues(children), [children]);

  const onSubmit = (values) => {
    try {
      dispatch(postChildrenThunk(values.children));
      toast.success('Дети успешно добавлены');
    } catch (e) {
      toast.error(e);
    }
  };

  return (
    <Formik
      enableReinitialize
      onSubmit={onSubmit}
      initialValues={initialValues}
      validationSchema={validationSchema}
    >
      {({ values }) => {
        const availablePush =
          values.children[values.children.length - 1].birthDate;

        return (
          <Form>
            <FieldArray name="children">
              {({ push, remove }) => {
                return (
                  <div style={{ marginBottom: 55 }}>
                    {values.children.map((child, idx, arr) => {
                      const biggerThanStartIndex = arr.length > 1;

                      return (
                        <div
                          key={String(`${idx}`)}
                          style={{ position: 'relative' }}
                        >
                          <Calendar name={`children.${idx}.birthDate`} />
                          <Gender name={`children.${idx}.sex`} />

                          {biggerThanStartIndex && (
                            <ButtonsAddRemoveChild
                              onClick={() => remove(idx)}
                            />
                          )}
                        </div>
                      );
                    })}

                    {amount !== values.children.length && (
                      <ButtonsAddRemoveChild
                        add
                        text={getTranslatedText('button.addField')}
                        onClick={() => {
                          if (availablePush) {
                            push({
                              birthDate: null,
                              sex: enumSex.UNSELECTED,
                            });
                          } else {
                            toast.error('Сначала надо выбрать дату рождения');
                          }
                        }}
                      />
                    )}
                  </div>
                );
              }}
            </FieldArray>

            <Button
              mb="220px"
              type="submit"
              width="248px"
              text={getTranslatedText('button.saveChanges')}
            />
          </Form>
        );
      }}
    </Formik>
  );
};

export { Children };
