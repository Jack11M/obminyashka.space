import { useState } from 'react';
import DatePicker, { registerLocale } from 'react-datepicker';
import { useSelector } from 'react-redux';

import { ru, enUS } from 'date-fns/locale';
// import eng from 'date-fns/locale/en-US';

import { getAuth } from 'store/auth/slice';
import { getTranslatedText } from 'components/local/localization';

import * as Styles from './styles';
import { withRef } from './withRef';

import 'react-datepicker/dist/react-datepicker.css';

const Calendar = () => {
  console.log(navigator.language);
  const { lang } = useSelector(getAuth);

  const isEng = lang === 'en';

  const l = isEng ? enUS : ru;

  registerLocale('lang', l);
  const [startDate, setStartDate] = useState(new Date());
  // const [endDate, setEndDate] = useState();

  // console.log(endDate);

  // eslint-disable-next-line react/no-unstable-nested-components
  // const ExampleCustomInput = forwardRef(({ value, onClick }, ref) => (
  //   // <button
  //   //   type="button"
  //   //   // className="example-custom-input"
  //   //   onClick={onClick}
  //   //   ref={ref}
  //   // >
  //   //   {value}
  //   // </button>
  //   <Styles.Input type="text" value={value} ref={ref} onClick={onClick} />
  // ));

  const A = withRef(Styles.Input);

  return (
    <Styles.Block>
      <Styles.Label htmlFor="date">
        {getTranslatedText('ownInfo.dateOfBirth')}
      </Styles.Label>
      {/* <Styles.Input id="data" type="date" /> */}
      <div>
        <DatePicker
          selected={startDate}
          dateFormat={isEng ? 'MM/dd/yyyy' : 'dd/MM/yyyy'}
          withPortal
          // fixedHeight
          locale="lang"
          showMonthDropdown
          showYearDropdown
          dateFormatCalendar="MMMM"
          yearDropdownItemNumber={19}
          scrollableYearDropdown
          // dateFormat="Pp"
          // minDate={subMonths(new Date(), 6)}
          calendarStartDay={isEng ? 0 : 1}
          onChange={(date) => setStartDate(date)}
          customInput={<A type="text" value={startDate} />}
        />
      </div>
    </Styles.Block>
  );
};

export { Calendar };
