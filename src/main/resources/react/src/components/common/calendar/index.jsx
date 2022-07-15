import { useMemo, useState } from 'react';
import { subYears } from 'date-fns';
import { useSelector } from 'react-redux';
import { ru, enUS, uk } from 'date-fns/locale';
import DatePicker, { registerLocale } from 'react-datepicker';

import { getAuth } from 'store/auth/slice';
import { getTranslatedText } from 'components/local/localization';

import { Header } from './header';
import * as Styles from './styles';
import { withRef } from './withRef';

import 'react-datepicker/dist/react-datepicker.css';

const Input = withRef(Styles.Input);

const YEARS_OLD = 14;

const Calendar = () => {
  const { lang } = useSelector(getAuth);
  const [startDate, setStartDate] = useState(new Date());

  const language = useMemo(() => {
    switch (lang) {
      case 'en':
        return enUS;
      case 'ru':
        return ru;
      default:
        return uk;
    }
  }, [lang]);

  const isEng = lang === 'en';

  registerLocale('lang', language);

  return (
    <Styles.Container>
      <Styles.Label htmlFor="date">
        {getTranslatedText('ownInfo.dateOfBirth')}
      </Styles.Label>

      <DatePicker
        withPortal
        fixedHeight
        locale="lang"
        maxDate={new Date()}
        selected={startDate}
        calendarStartDay={isEng ? 0 : 1}
        onChange={(date) => setStartDate(date)}
        minDate={subYears(new Date(), YEARS_OLD)}
        dateFormat={isEng ? 'MM/dd/yyyy' : 'dd-MM-yyyy'}
        customInput={<Input type="text" value={startDate} />}
        renderCustomHeader={(props) => (
          <Header {...props} lang={lang} yearsOld={YEARS_OLD} />
        )}
        popperModifiers={[
          {
            name: 'preventOverflow',
            options: {
              rootBoundary: 'viewport',
            },
          },
        ]}
      />
    </Styles.Container>
  );
};

export { Calendar };
