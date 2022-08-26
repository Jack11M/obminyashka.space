import { useField } from 'formik';

import { CloseSvg } from 'assets/icons';
import { ErrorDisplay } from 'pages/AddGoods/error-display';

import * as Styles from './styles';

const DropDownInput = ({
  name,
  size,
  data,
  title,
  value,
  focus,
  onFocus,
  disabled,
  showDrop,
  choiceItem,
  clearInput,
  onChangeInput,
  checkInputValue,
}) => {
  const [, meta] = useField({ name: 'locationId' });
  const { error } = meta;

  return (
    <Styles.Wrap>
      {!size && <Styles.Label>{title}:</Styles.Label>}
      <Styles.Input
        name={name}
        focus={focus}
        error={error}
        value={value}
        onFocus={onFocus}
        autocomplete="off"
        disabled={disabled}
        onChange={onChangeInput}
      />
      {checkInputValue && (
        <Styles.WrapSvg onClick={clearInput}>
          <CloseSvg />
        </Styles.WrapSvg>
      )}
      <ErrorDisplay error={!!error && error} />

      {showDrop && (
        <Styles.WrapDropItems showDrop={showDrop}>
          {data.map((item, index) => (
            <Styles.SelectedItem
              key={String(`${item}_${index}`)}
              onClick={() => choiceItem(item)}
            >
              <p>{item}</p>
            </Styles.SelectedItem>
          ))}
        </Styles.WrapDropItems>
      )}
    </Styles.Wrap>
  );
};
export { DropDownInput };
