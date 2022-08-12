import { useState } from 'react';

import * as Icon from 'assets/icons';
import { useDelay } from 'Utils/delay';
import { EllipsisText, Avatar, CropImage } from 'components/common';

import * as Styles from './styles';
import { getName } from './helpers';

const ActiveProfile = ({ firstName, lastName, source }) => {
  const [image, setImage] = useState(source);
  const [showIcon, setShowIcon] = useDelay(300);

  console.log(setImage);

  return (
    <>
      <Styles.ProfileBlock>
        <Styles.ProfileBox>
          <Styles.WrapAvatar
            hasImage={!!image}
            showIcon={showIcon}
            onMouseEnter={() => setShowIcon(true)}
            onMouseLeave={() => setShowIcon(false)}
          >
            <Avatar width={135} height={135} source={image} />

            {showIcon && (
              <Styles.WrapCropSvg>
                <Icon.CropSvg />
              </Styles.WrapCropSvg>
            )}
          </Styles.WrapAvatar>

          <Styles.BoxData>
            <Styles.DataName>
              <EllipsisText>{getName(firstName, lastName)}</EllipsisText>
            </Styles.DataName>
          </Styles.BoxData>
        </Styles.ProfileBox>
      </Styles.ProfileBlock>

      <CropImage />
    </>
  );
};

export { ActiveProfile };
