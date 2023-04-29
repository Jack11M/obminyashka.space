import { useMemo, useState } from 'react';
import { FieldArray, Form, Formik } from 'formik';
import { useDispatch, useSelector } from 'react-redux';
import { Button, Icon, showMessage } from 'obminyashka-components';

import { enumSex } from 'src/config/ENUM';
import { getProfile } from 'src/store/profile/slice';
import { putChildrenThunk } from 'src/store/profile/thunk';
import { getTranslatedText } from 'src/components/local/localization';

import { Gender } from './gender';
import * as Styled from '../styles';
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
      showMessage.success(getTranslatedText('toastText.changedData'));
    } catch (e) {
      showMessage.error(e);
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
        const availablePush = values.children[values.children.length - 1].birthDate;

        return (
          <Form>
            <FieldArray name='children'>
              {({ push, remove }) => {
                return (
                  <div style={{ marginBottom: 55 }}>
                    {values.children.map((child, idx, arr) => {
                      const biggerThanStartIndex = arr.length > 1;

                      return (
                        <div key={String(`${idx}`)} style={{ position: 'relative' }}>
                          <Calendar name={`children.${idx}.birthDate`} />
                          <Gender name={`children.${idx}.sex`} />

                          {biggerThanStartIndex && (
                            <Styled.WrapperDelButton>
                              <Button
                                gap={34}
                                width={34}
                                height={34}
                                outsideText
                                colorType='grey'
                                icon={<Icon.Plus />}
                                onClick={() => remove(idx)}
                              />
                            </Styled.WrapperDelButton>
                          )}
                        </div>
                      );
                    })}

                    {amount !== values.children.length && (
                      <Styled.WrapperAddButton>
                        <Button
                          gap={20}
                          width={34}
                          height={34}
                          outsideText
                          colorType='green'
                          icon={<Icon.Plus />}
                          text={getTranslatedText('button.addField')}
                          onClick={() => {
                            if (availablePush) {
                              push({
                                birthDate: null,
                                sex: enumSex.UNSELECTED,
                              });
                            } else {
                              showMessage.error(getTranslatedText('ownInfo.chooseData'));
                            }
                          }}
                        />
                      </Styled.WrapperAddButton>
                    )}
                  </div>
                );
              }}
            </FieldArray>

            <Button
              width={248}
              type='submit'
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
