import { useState } from 'react';

import * as Icon from 'assets/icons';

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

      <div>
        {openSelect && <Styles.SubTitleBlock>{children}</Styles.SubTitleBlock>}
      </div>
    </div>
  );
};

export { CheckBoxes };
