import { useState } from 'react';
import { useSelector, useDispatch } from 'react-redux';
import {
  CroppedImage,
  EllipsisText,
  showMessage,
} from 'obminyashka-components';

import { getErrorMessage } from 'Utils/error';
import { getProfile } from 'store/profile/slice';
import { getTranslatedText } from 'components/local';
import { deleteAvatarThunk, postAvatarThunk } from 'store/profile/thunk';

import * as Styles from './styles';
import { getName } from './helpers';

const ActiveProfile = () => {
  const profile = useSelector(getProfile);
  const { firstName, lastName, avatarImage, email } = profile || {};
  const [isSaveLoading, setIsSaveLoading] = useState(false);
  const [isDeleteLoading, setIsDeleteLoading] = useState(false);

  const dispatch = useDispatch();
  const cropDelete = async (props) => {
    const { handleClear, setOpenCrop } = props;
    try {
      setIsDeleteLoading(true);
      await dispatch(deleteAvatarThunk());
      handleClear();
      setOpenCrop(false);
    } catch (e) {
      showMessage.error(getErrorMessage(e));
    } finally {
      setIsDeleteLoading(false);
    }
  };
  const cropSave = async (props) => {
    const { file, setOpenCrop, handleSetImage } = props;
    try {
      setIsSaveLoading(true);

      const dataForm = new FormData();
      dataForm.append('image', file);

      const receivedImage = await dispatch(postAvatarThunk(dataForm));
      await handleSetImage(receivedImage);
      setOpenCrop(false);
    } catch (e) {
      showMessage.error(getErrorMessage(e));
    } finally {
      setIsSaveLoading(false);
    }
  };
  return (
    <Styles.ProfileBlock>
      <Styles.ProfileBox>
        <CroppedImage
          avatarImage={avatarImage}
          isSaveLoading={isSaveLoading}
          isDeleteLoading={isDeleteLoading}
          onSave={(props) => cropSave(props)}
          saveBtnText={getTranslatedText('button.save')}
          errorSize={getTranslatedText('popup.sizeFile')}
          cropTitle={getTranslatedText('popup.addPhoto')}
          onDelete={(props) => cropDelete(props)}
          closeBtnText={getTranslatedText('button.cancel')}
          deleteBtnText={getTranslatedText('button.delete')}
          rotateBtnText={getTranslatedText('button.rotate')}
          errorTitle={getTranslatedText('popup.errorTitle')}
          errorSizeSelect={getTranslatedText('popup.selectFile')}
          errorFormat={getTranslatedText('popup.pictureSelection')}
        />
        <Styles.BoxData>
          <Styles.DataName>
            <EllipsisText id="showUserData">
              {getName(firstName, lastName) || email}
            </EllipsisText>
          </Styles.DataName>
        </Styles.BoxData>
      </Styles.ProfileBox>
    </Styles.ProfileBlock>
  );
};
export { ActiveProfile };
