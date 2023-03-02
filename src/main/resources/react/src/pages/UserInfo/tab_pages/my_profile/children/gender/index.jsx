import { useField } from 'formik';
import { CheckBox } from 'obminyashka-components';

import { enumSex } from 'config/ENUM';
import { getTranslatedText } from 'components/local/localization';

import * as Styles from './styles';

const Gender = ({ name }) => {
  const [field, , helpers] = useField(name);
  const { value } = field;

  return (
    <Styles.GenderDiv>
      <Styles.Title>{getTranslatedText('ownInfo.gender')}</Styles.Title>

      <Styles.Container>
        <CheckBox
          type="radio"
          fontSize={16}
          checked={value === enumSex.MALE}
          text={getTranslatedText('ownInfo.boy')}
          onChange={() => helpers.setValue(enumSex.MALE)}
        />

        <CheckBox
          type="radio"
          fontSize={16}
          checked={value === enumSex.FEMALE}
          text={getTranslatedText('ownInfo.girl')}
          onChange={() => helpers.setValue(enumSex.FEMALE)}
        />

        <CheckBox
          type="radio"
          fontSize={16}
          checked={value === enumSex.UNSELECTED}
          text={getTranslatedText('ownInfo.unselected')}
          onChange={() => helpers.setValue(enumSex.UNSELECTED)}
        />
      </Styles.Container>
    </Styles.GenderDiv>
  );
};

export { Gender };
