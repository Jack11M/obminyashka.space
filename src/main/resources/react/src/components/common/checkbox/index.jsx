import { CheckSvg } from 'assets/icons';

import { Div, LabelSquare, Label } from './styles';

const CheckBox = ({
  margin,
  click = null,
  checked = false,
  distanceBetween = false,
  fontSize,
  text,
}) => (
  <Div margin={margin} onClick={click} checked={checked}>
    <LabelSquare checked={checked}>
      <CheckSvg />
    </LabelSquare>

    <div>
      <Label
        checked={checked}
        fontSize={fontSize}
        distanceBetween={distanceBetween}
      >
        {text}
      </Label>
    </div>
  </Div>
);

export { CheckBox };
