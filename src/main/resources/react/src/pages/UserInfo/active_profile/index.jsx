import { useSelector } from 'react-redux';

import { getProfile } from 'store/profile/slice';
import { EllipsisText } from 'components/common';

import * as Styles from './styles';
import { getName } from './helpers';
import { CropImage } from './crop-image';

const ActiveProfile = () => {
  const { firstName, lastName, avatarImage } = useSelector(getProfile);

  return (
    <Styles.ProfileBlock>
      <Styles.ProfileBox>
        <CropImage image={avatarImage} />

        <Styles.BoxData>
          <Styles.DataName>
            <EllipsisText>{getName(firstName, lastName)}</EllipsisText>
          </Styles.DataName>
        </Styles.BoxData>
      </Styles.ProfileBox>
    </Styles.ProfileBlock>
  );
};

export { ActiveProfile };
