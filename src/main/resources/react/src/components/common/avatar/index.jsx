import { useEffect, useState } from 'react';

import * as Icon from 'assets/icons';

import { Image } from '../image';

const string = 'data:image/jpeg;base64,';

const Avatar = ({ width = 30, height = 28, source }) => {
  const [image, setImage] = useState(null);

  useEffect(() => {
    if (!source?.includes(string) && source) {
      setImage(`${string}${source}`);
    }

    if (source?.includes(string) && source) {
      setImage(source);
    }

    if (!source) setImage(null);
  }, [source]);

  if (!source) {
    return <Icon.AvatarSvg width={width} height={height} />;
  }

  return <Image source={image} width={width} height={height} />;
};

export { Avatar };
