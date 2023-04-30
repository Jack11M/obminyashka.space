/* eslint-disable */
// @ts-nocheck
// TODO: fix typescript
import { useState } from 'react';
import { Icon } from 'obminyashka-components';

import * as Styles from './styles';

const CheckBoxes = ({ title, children }) => {
  const [openSelect, setOpenSelect] = useState(false);

  return (
    <div>
      <Styles.TitleBlock onClick={() => setOpenSelect(!openSelect)}>
        <Styles.Title>{title}</Styles.Title>

        <Styles.RotateRectangle openSelect={openSelect}>
          <Icon.Rectangle />
        </Styles.RotateRectangle>
      </Styles.TitleBlock>

      {openSelect && <Styles.SubTitleBlock>{children}</Styles.SubTitleBlock>}
    </div>
  );
};

export { CheckBoxes };
