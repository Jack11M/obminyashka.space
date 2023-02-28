import { useMemo, useState } from 'react';
import { toast } from 'react-toastify';
import { FieldArray, Form, Formik } from 'formik';
import { useSelector, useDispatch } from 'react-redux';
import { Button } from '@wolshebnik/obminyashka-components';

import { enumSex } from 'config/ENUM';
import { getProfile } from 'store/profile/slice';
import { putChildrenThunk } from 'store/profile/thunk';
import { getTranslatedText } from 'components/local/localization';
import ButtonsAddRemoveChild from 'pages/UserInfo/components/buttonsAddRemoveChild';

import { Gender } from './gender';
import { Calendar } from './calendar';
import { getInitialValues, validationSchema } from './config';

const amount = 10;

const Children = () => {
  const dispatch = useDispatch();
  const profile = useSelector(getProfile);
  const [isLoading, setIsLoading] = useState(false);

  const { children } = profile || {};

  const initialValues = useMemo(() => getInitialValues(children), [children]);

  const onSubmit = async (values) => {
    setIsLoading(true);
    try {
      await dispatch(putChildrenThunk(values.children));
      toast.success(getTranslatedText('toastText.changedData'));
    } catch (e) {
      toast.error(e);
    } finally {
      setIsLoading(false);
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
                            toast.error(
                              getTranslatedText('ownInfo.chooseData')
                            );
                          }
                        }}
                      />
                    )}
                  </div>
                );
              }}
            </FieldArray>

            <Button
              width={248}
              type="submit"
              isLoading={isLoading}
              style={{ marginBottom: 220 }}
              text={getTranslatedText('button.saveChanges')}
            />
          </Form>
        );
      }}
    </Formik>
  );
};

export { Children };
