import React, { useEffect } from 'react';
import { useDispatch, useSelector } from 'react-redux';

import { getProfile } from 'store/profile/slice';
import { getUserThunk } from 'store/profile/thunk';
import { getTranslatedText } from 'components/local/localization';
import { TitleBigBlue, Button } from 'components/common';

import { AboutMyself } from './about-myself';
import InputProfile from '../../components/inputProfile';
import InputGender from '../../components/inputProfile/inputGender';

import './myProfile.scss';

const MyProfile = () => {
  const dispatch = useDispatch();
  const { children } = useSelector(getProfile);

  useEffect(() => {
    dispatch(getUserThunk());
  }, [dispatch]);

  return (
    <>
      <TitleBigBlue
        whatClass="myProfile-title"
        text={getTranslatedText('ownInfo.aboutMe')}
      />

      <AboutMyself />

      <TitleBigBlue
        whatClass="myProfile-title"
        text={getTranslatedText('ownInfo.children')}
      />

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
    </>
  );
};
export default React.memo(MyProfile);
