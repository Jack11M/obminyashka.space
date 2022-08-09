import { Avatar } from 'components/common/avatar';

import * as Styles from './styles';
import { getName } from './helpers';

const ActiveProfile = ({ firstName, lastName, source }) => (
  <Styles.ProfileBlock>
    <Styles.ProfileBox>
      <Avatar width={135} height={135} source={source} />

      <Styles.BoxData>
        <Styles.DataName>{getName(firstName, lastName)}</Styles.DataName>
      </Styles.BoxData>
    </Styles.ProfileBox>
  </Styles.ProfileBlock>
);

export { ActiveProfile };
