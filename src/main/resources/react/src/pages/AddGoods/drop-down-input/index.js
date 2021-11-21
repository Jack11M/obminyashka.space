import { CloseSvg } from 'assets/icons';

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
  return (
    <Wrap>
      <Label>
        <span className="span_star">*</span> {title}:
      </Label>
      <Input
        name={name}
        onFocus={onFocus}
        focus={focus}
        autocomplete="off"
        onChange={onChangeInput}
        value={value}
        disabled={disabled}
      />
      {checkInputValue && (
        <WrapSvg onClick={clearInput}>
          <CloseSvg />
        </WrapSvg>
      )}

      {showDrop && (
        <WrapDropItems showDrop={showDrop}>
          {data.map((item, index) => (
            <SelectedItem
              key={`${item}_${index}`}
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
