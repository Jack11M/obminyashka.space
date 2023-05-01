/* eslint-disable */
// @ts-nocheck
// TODO: fix typescript
import { useMemo } from 'react';
import { useField } from 'formik';
import dayjs from 'dayjs';
import { subYears } from 'date-fns';
import { useSelector } from 'react-redux';
import { enUS, uk } from 'date-fns/locale';
import DatePicker, { registerLocale } from 'react-datepicker';

import { getAuth } from 'src/store/auth/slice';
import { getTranslatedText } from 'src/components/local/localization';

import { Header } from './header';
import * as Styles from './styles';
import { withRef } from './withRef';

import 'react-datepicker/dist/react-datepicker.css';

const Input = withRef(Styles.Input);

const YEARS_OLD = 14;

const Calendar = ({ name }: { name: string }) => {
  const { lang } = useSelector(getAuth);
  const [field, meta, helpers] = useField(name);
  const { value } = field;

  const language = useMemo(() => {
    switch (lang) {
      case 'en':
        return enUS;
      default:
        return uk;
    }
  }, [lang]);

  const isEng = lang === 'en';
  registerLocale('lang', language);

  return (
    <Styles.Container>
      <Styles.Label htmlFor='date'>{getTranslatedText('ownInfo.dateOfBirth')}</Styles.Label>
      <div>
        <DatePicker
          withPortal
          fixedHeight
          locale='lang'
          maxDate={new Date()}
          calendarStartDay={isEng ? 0 : 1}
          minDate={subYears(new Date(), YEARS_OLD)}
          selected={value ? dayjs(value).toDate() : null}
          dateFormat={isEng ? 'MM/dd/yyyy' : 'dd/MM/yyyy'}
          customInput={<Input type='text' value={value} error={meta.touched && meta.error} />}
          onChange={(date) => helpers.setValue(dayjs(date).format('YYYY-MM-DD'))}
          renderCustomHeader={(props) => <Header {...props} lang={lang} yearsOld={YEARS_OLD} />}
          popperModifiers={[
            {
              name: 'preventOverflow',
              options: {
                rootBoundary: 'viewport',
              },
            },
          ]}
        />

        {meta.touched && meta.error && <Styles.ErrorSpan>{meta.error}</Styles.ErrorSpan>}
      </div>
    </Styles.Container>
  );
};

export { Calendar };
