import { useSelector } from 'react-redux';

import { Button } from 'components/common';
import { getProfile } from 'store/profile/slice';
import { getTranslatedText } from 'components/local/localization';

import InputProfile from '../../../components/inputProfile';
import InputGender from '../../../components/inputProfile/inputGender';

const Children = () => {
  const { children } = useSelector(getProfile);

  return (
    <form>
      <div className="block-children">
        {children.map((child, idx) => (
          <div className="block-child" key={String(`${idx}_child`)}>
            <InputProfile
              id={idx}
              type="date"
              onChange={null}
              name="birthDate"
              value={child.birthDate}
              label={getTranslatedText('ownInfo.dateOfBirth')}
            />

            <InputGender gender={child.sex} id={idx} click={null} />
          </div>
        ))}
      </div>

      <Button
        width="248px"
        disabling={false}
        whatClass="btn-form-children"
        text={getTranslatedText('button.saveChanges')}
      />
    </form>
  );
};

export { Children };
