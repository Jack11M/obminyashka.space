import { useState } from 'react';

// import ua from 'components/local/ua';
// import { enumAge } from 'config/ENUM';
import { getTranslatedText } from 'components/local/localization';

import { CheckBox } from 'components/common';

import * as Icon from 'assets/icons';

import * as Styles from './styles';

const CheckBoxes = ({ title, data, dataText }) => {
  const [openSelect, setOpenSelect] = useState(false);
  const [checkbox, setCheckbox] = useState(false);

  const changeCheckBox = () => {
    setCheckbox((prev) => !prev);
  };

  return (
    <div>
      <Styles.TitleBlock onClick={() => setOpenSelect(!openSelect)}>
        <Styles.Title>{title}</Styles.Title>

        <Styles.RotateRectangle openSelect={openSelect}>
          <Icon.Rectangle />
        </Styles.RotateRectangle>
      </Styles.TitleBlock>

      <Styles.OptionWrapper hideSelect={openSelect}>
        {openSelect &&
          data.map((item, idx) => (
            <Styles.SubTitleBlock key={String(item + idx)}>
              <CheckBox
                fontSize="16px"
                checked={checkbox}
                click={changeCheckBox}
                margin="4px 8px 4px 58px"
                text={getTranslatedText(`${dataText}.${item}`)}
              />
            </Styles.SubTitleBlock>
          ))}
      </Styles.OptionWrapper>
    </div>
  );
};

export { CheckBoxes };
