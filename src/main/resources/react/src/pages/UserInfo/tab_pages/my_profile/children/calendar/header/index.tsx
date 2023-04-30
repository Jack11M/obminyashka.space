/* eslint-disable */
// @ts-nocheck
// TODO: fix typescript
import { useMemo } from 'react';
import dayjs from 'dayjs';

import { range } from './helpers';
import * as Styles from './styles';
import { Select } from '../select';
import { uaMonths, enMonths } from './config';

const Header = ({
  lang,
  date,
  yearsOld,
  changeYear,
  changeMonth,
  decreaseMonth,
  increaseMonth,
  prevMonthButtonDisabled,
  nextMonthButtonDisabled,
}) => {
  const years = useMemo(
    () => range(dayjs(new Date()).year() - yearsOld, dayjs(new Date()).year() + 1),
    [yearsOld]
  );

  const months = useMemo(() => {
    switch (lang) {
      case 'en':
        return enMonths;
      default:
        return uaMonths;
    }
  }, [lang]);

  return (
    <Styles.Header>
      <Styles.ChevronLeft onClick={decreaseMonth} disabled={prevMonthButtonDisabled} />

      <Styles.DateContainer>
        <Select
          data={months}
          changeMonth={changeMonth}
          selectedValue={months[dayjs(date).month()]}
        />

        <Select data={years} changeYear={changeYear} selectedValue={dayjs(date).year()} />
      </Styles.DateContainer>

      <Styles.ChevronRight onClick={increaseMonth} disabled={nextMonthButtonDisabled} />
    </Styles.Header>
  );
};

export { Header };
