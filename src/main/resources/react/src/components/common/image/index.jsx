import { useState } from 'react';

import * as Icon from 'assets/icons';

import * as Styles from './styles';

const Image = ({ width = 30, height = 28, source }) => {
  const [isLoading, setIsLoading] = useState(true);

  return (
    <Styles.AvatarBlock
      style={{
        height,
        width,
      }}
    >
      <Styles.Image
        alt="avatar"
        src={source}
        width={width}
        height={height}
        isLoading={isLoading}
        onLoad={() => setIsLoading(false)}
        onError={() => setIsLoading(false)}
      />

      {isLoading && <Icon.LoaderSvg width={width} height={height} />}
    </Styles.AvatarBlock>
  );
};

export { Image };
