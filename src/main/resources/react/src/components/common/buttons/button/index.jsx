import { Loader } from 'components/common';

import { ButtonBlue, WrapIcon } from './styles';

const Button = ({
  mb,
  type,
  bold,
  icon,
  text,
  width,
  style,
  lHeight,
  isLoading,
  disabling,
  click = null,
  whatClass = null,
  ...props
}) => (
  <ButtonBlue
    mb={mb}
    type={type}
    bold={bold}
    width={width}
    style={style}
    lHeight={lHeight}
    disabled={disabling}
    className={whatClass}
    onClick={!isLoading ? click : undefined}
    {...props}
  >
    {isLoading ? <Loader /> : text}
    {icon && <WrapIcon>{icon}</WrapIcon>}
  </ButtonBlue>
);
export { Button };
