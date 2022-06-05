import { getTranslatedText } from 'components/local/localization';

import * as Styles from './styles';

const InputGender = ({ gender, id, click }) => (
  <Styles.GenderDiv>
    <Styles.LabelGander>
      {getTranslatedText('ownInfo.gender')}
    </Styles.LabelGander>

    <Styles.Container>
      <Styles.ChildDiv onClick={() => click(id, 'MALE')}>
        <Styles.CircleBoy gender={gender} />
        <Styles.Span>{getTranslatedText('ownInfo.boy')}</Styles.Span>
      </Styles.ChildDiv>

      <Styles.ChildDiv onClick={() => click(id, 'FEMALE')}>
        <Styles.CircleGirl gender={gender} />
        <Styles.Span>{getTranslatedText('ownInfo.girl')}</Styles.Span>
      </Styles.ChildDiv>

      <Styles.ChildDiv onClick={() => click(id, 'UNSELECTED')}>
        <Styles.CircleUnselected gender={gender} />
        <Styles.Span>{getTranslatedText('ownInfo.unselected')}</Styles.Span>
      </Styles.ChildDiv>
    </Styles.Container>
  </Styles.GenderDiv>
);

export default InputGender;
