import { useCallback, useRef, useState } from 'react';
import Cropper from 'react-easy-crop';
import { useClickAway } from 'react-use';
import { useDispatch } from 'react-redux';
import { Button, Icon } from '@wolshebnik/obminyashka-components';

import { showMessage } from 'hooks';
import { getErrorMessage } from 'Utils/error';
import { getTranslatedText } from 'components/local';
import { deleteAvatarThunk, postAvatarThunk } from 'store/profile/thunk';

import * as Styles from './styles';
import getCroppedImg from './helpers';

const Crop = ({ image, onClose, setImage, setCroppedImage, disabled }) => {
  const ref = useRef(null);
  const dispatch = useDispatch();

  const [crop, setCrop] = useState({ x: 0, y: 0 });
  const [zoom, setZoom] = useState(1);
  const [rotation, setRotation] = useState(0);
  const [croppedAreaPixels, setCroppedAreaPixels] = useState(null);
  const [loading, setLoading] = useState(false);
  const [loadingDelete, setLoadingDelete] = useState(false);

  const onCropComplete = useCallback((_, croppedPixels) => {
    setCroppedAreaPixels(croppedPixels);
  }, []);

  const showCroppedImage = useCallback(async () => {
    try {
      setLoading(true);
      const croppedPicture = await getCroppedImg(
        image,
        croppedAreaPixels,
        rotation
      );

      if (typeof croppedPicture?.src === 'string') {
        const file = new File([croppedPicture.blob], 'avatar', {
          type: croppedPicture.blob.type,
        });

        const dataForm = new FormData();
        dataForm.append('image', file);

        const receivedImage = await dispatch(
          postAvatarThunk(dataForm, croppedPicture.src)
        );

        setImage(receivedImage);
        setCroppedImage(receivedImage);
        onClose();
      }
    } catch (e) {
      showMessage(getErrorMessage(e));
    } finally {
      setLoading(false);
    }
  }, [image, croppedAreaPixels, rotation]);

  const handleDelete = async () => {
    try {
      setLoadingDelete(true);
      await dispatch(deleteAvatarThunk());
      setImage(null);
      onClose();
    } catch (e) {
      showMessage(getErrorMessage(e));
    } finally {
      setLoadingDelete(false);
    }
  };

  useClickAway(ref, () => {
    onClose();
  });

  return (
    <Styles.Overlay>
      <Styles.Container ref={ref}>
        <Styles.Wrap>
          <Styles.Title>{getTranslatedText('popup.addPhoto')}</Styles.Title>

          <Styles.BlockCrop>
            <Cropper
              aspect={1}
              crop={crop}
              zoom={zoom}
              image={image}
              cropShape="round"
              rotation={rotation}
              onZoomChange={setZoom}
              onCropChange={setCrop}
              objectFit="vertical-cover"
              onCropComplete={onCropComplete}
            />
          </Styles.BlockCrop>

          <Styles.RotationBlock
            onClick={() => setRotation((prev) => prev + 45)}
          >
            <Icon.Rotate />
            <Styles.Text>{getTranslatedText('button.rotate')}</Styles.Text>
          </Styles.RotationBlock>

          <Styles.BlockButtons>
            {!disabled && (
              <Button
                onClick={handleDelete}
                isLoading={loadingDelete}
                style={{ width: '100%' }}
                text={getTranslatedText('button.delete')}
              />
            )}

            <Button
              onClick={onClose}
              text={getTranslatedText('button.cancel')}
            />

            <Button
              isLoading={loading}
              onClick={showCroppedImage}
              text={getTranslatedText('button.save')}
            />
          </Styles.BlockButtons>
        </Styles.Wrap>
      </Styles.Container>
    </Styles.Overlay>
  );
};

export { Crop };
