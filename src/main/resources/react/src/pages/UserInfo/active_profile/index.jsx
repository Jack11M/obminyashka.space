import { useSelector } from 'react-redux';
import { EllipsisText } from 'obminyashka-components';

import { getProfile } from 'store/profile/slice';

import * as Styles from './styles';
import { getName } from './helpers';
import { CropImage } from './crop-image';

const ActiveProfile = () => {
  const profile = useSelector(getProfile);
  const { firstName, lastName, avatarImage, email } = profile || {};

  return (
    <Styles.ProfileBlock>
      <Styles.ProfileBox>
        <CropImage avatarImage={avatarImage} />

        <Styles.BoxData>
          <Styles.DataName>
            <EllipsisText id="showUserData">
              {getName(firstName, lastName) || email}
            </EllipsisText>
          </Styles.DataName>
        </Styles.BoxData>
      </Styles.ProfileBox>
    </Styles.ProfileBlock>
  );
};

export { ActiveProfile };
