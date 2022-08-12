import * as Icon from 'assets/icons';

import { Image } from '../image';

const Avatar = ({ width = 30, height = 28, source }) => {
  if (!source) {
    return <Icon.AvatarSvg width={width} height={height} />;
  }

  return <Image source={source} width={width} height={height} />;
};

export { Avatar };
