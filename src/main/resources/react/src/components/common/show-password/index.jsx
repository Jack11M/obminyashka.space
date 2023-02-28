import { useMemo, useState } from 'react';
import { Icon } from '@wolshebnik/obminyashka-components';

import { WrapIcon } from './styles';

const showPassword = (isPassword, distance) => {
  const [isShow, setIsShow] = useState(isPassword);

  const currentType = useMemo(() => {
    return isShow ? 'text' : 'password';
  }, [isShow]);

  const Component = (
    <WrapIcon onClick={() => setIsShow(!isShow)} distance={distance}>
      {isShow ? <Icon.Eye /> : <Icon.EyeOff />}
    </WrapIcon>
  );
  return {
    component: Component,
    currentType,
  };
};

export { showPassword };
