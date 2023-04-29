import { useState } from 'react';
import { Icon } from 'obminyashka-components';

import { getTranslatedText } from 'components/local/localization';

import * as Styles from './styles';

const InputsWithLocation = () => {
  const [openSelect, setOpenSelect] = useState(false);

  return (
    <div>
      <Styles.TitleBlock onClick={() => setOpenSelect(!openSelect)}>
        <Styles.Title>{getTranslatedText('filterPage.location')}</Styles.Title>

        <Styles.RotateRectangle openSelect={openSelect}>
          <Icon.Rectangle />
        </Styles.RotateRectangle>
      </Styles.TitleBlock>

      <Styles.OptionWrapper hideSelect={openSelect}>
        {openSelect && (
          <>
            <Styles.SubTitleBlock>
              <Styles.Input
                placeholder={getTranslatedText('filterPage.locationArea')}
              />
            </Styles.SubTitleBlock>

            <Styles.SubTitleBlock>
              <Styles.Input
                placeholder={getTranslatedText('filterPage.locationCity')}
              />
            </Styles.SubTitleBlock>
          </>
        )}
      </Styles.OptionWrapper>
    </div>
  );
};

export { InputsWithLocation };
