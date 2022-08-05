import { useField } from 'formik';

import { CloseSvg } from 'assets/icons';

import { ErrorDisplay } from '../error-display';

import {
  Wrap,
  Input,
  Label,
  WrapSvg,
  SelectedItem,
  WrapDropItems,
} from './styles';

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
    <Wrap>
      {!size && (
        <Label>
          <span className="span_star">*</span> {title}:
        </Label>
      )}
      <Input
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
        <WrapSvg onClick={clearInput}>
          <CloseSvg />
        </WrapSvg>
      )}
      <ErrorDisplay error={!!error && error} />

      {showDrop && (
        <WrapDropItems showDrop={showDrop}>
          {data.map((item, index) => (
            <SelectedItem
              key={String(`${item}_${index}`)}
              onClick={() => choiceItem(item)}
            >
              <p>{item}</p>
            </SelectedItem>
          ))}
        </WrapDropItems>
      )}
    </Wrap>
  );
};
export { DropDownInput };
