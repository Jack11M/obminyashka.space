import { useState } from 'react';

import { getTranslatedText } from 'components/local/localization';

import * as Icon from 'assets/icons';

import * as Styles from './styles';

const LocationInputs = () => {
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

export { LocationInputs };
