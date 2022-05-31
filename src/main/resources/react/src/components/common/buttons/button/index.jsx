import SpinnerForAuthBtn from 'components/common/spinner';

import { ButtonBlue, WrapIcon } from './styles';

const Button = ({
  mb,
  type,
  bold,
  icon,
  text,
  width,
  lHeight,
  isLoading,
  click = null,
  whatClass = null,
  disabling = null,
}) => (
  <ButtonBlue
    mb={mb}
    type={type}
    bold={bold}
    width={width}
    onClick={click}
    lHeight={lHeight}
    disabled={disabling}
    className={whatClass}
  >
    {isLoading ? <SpinnerForAuthBtn /> : text}
    {icon && <WrapIcon>{icon}</WrapIcon>}
  </ButtonBlue>
);
export { Button };
