import { useField } from 'formik';
import { CheckBox } from '@wolshebnik/obminyashka-components';

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
          onChange={() => helpers.setValue(enumSex.MALE)}
          checked={value === enumSex.MALE}
          text={getTranslatedText('ownInfo.boy')}
        />
        <CheckBox
          type="radio"
          fontSize={16}
          onChange={() => helpers.setValue(enumSex.FEMALE)}
          checked={value === enumSex.FEMALE}
          text={getTranslatedText('ownInfo.girl')}
        />
        <CheckBox
          type="radio"
          fontSize={16}
          onChange={() => helpers.setValue(enumSex.UNSELECTED)}
          checked={value === enumSex.UNSELECTED}
          text={getTranslatedText('ownInfo.unselected')}
        />
      </Styles.Container>
    </Styles.GenderDiv>
  );
};

export { Gender };
