import { useField } from 'formik';

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
        <Styles.ChildDiv onClick={() => helpers.setValue(enumSex.MALE)}>
          <Styles.Circle selected={value === enumSex.MALE} />
          <Styles.LabelGander>
            {getTranslatedText('ownInfo.boy')}
          </Styles.LabelGander>
        </Styles.ChildDiv>

        <Styles.ChildDiv onClick={() => helpers.setValue(enumSex.FEMALE)}>
          <Styles.Circle selected={value === enumSex.FEMALE} />
          <Styles.LabelGander>
            {getTranslatedText('ownInfo.girl')}
          </Styles.LabelGander>
        </Styles.ChildDiv>

        <Styles.ChildDiv onClick={() => helpers.setValue(enumSex.UNSELECTED)}>
          <Styles.Circle selected={value === enumSex.UNSELECTED} />
          <Styles.LabelGander>
            {getTranslatedText('ownInfo.unselected')}
          </Styles.LabelGander>
        </Styles.ChildDiv>
      </Styles.Container>
    </Styles.GenderDiv>
  );
};

export { Gender };
