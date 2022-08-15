import { useCallback, useRef, useState } from 'react';
import Cropper from 'react-easy-crop';
import { useClickAway } from 'react-use';
import { useDispatch } from 'react-redux';

import { showMessage } from 'hooks';
import * as Icon from 'assets/icons';
import { Button } from 'components/common';
import { getErrorMessage } from 'Utils/error';
import { postAvatarThunk } from 'store/profile/thunk';

import * as Styles from './styles';
import getCroppedImg from './helpers';

const Crop = ({ image, onClose, setImage, setCroppedImage }) => {
  const dispatch = useDispatch();
  const ref = useRef(null);
  const [crop, setCrop] = useState({ x: 0, y: 0 });
  const [zoom, setZoom] = useState(1);
  const [rotation, setRotation] = useState(0);
  const [croppedAreaPixels, setCroppedAreaPixels] = useState(null);

  const onCropComplete = useCallback((_, croppedPixels) => {
    setCroppedAreaPixels(croppedPixels);
  }, []);

  const showCroppedImage = useCallback(async () => {
    try {
      const croppedPicture = await getCroppedImg(
        image,
        croppedAreaPixels,
        rotation
      );
      await dispatch(postAvatarThunk({ file: croppedPicture }));
      setImage(croppedPicture);
      setCroppedImage(croppedPicture);
      onClose();
    } catch (e) {
      showMessage(getErrorMessage(e));
    }
  }, [image, croppedAreaPixels, rotation]);

  const handleDelete = () => {
    setImage(null);
    onClose();
  };

  useClickAway(ref, () => {
    onClose();
  });

  return (
    <Styles.Overlay>
      <Styles.Container ref={ref}>
        <Styles.Wrap>
          <Styles.Title>Add photo</Styles.Title>

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
            <Styles.Text>Rotate</Styles.Text>
          </Styles.RotationBlock>

          <Styles.BlockButtons>
            <Button
              text="Delete"
              click={handleDelete}
              style={{ width: '100%' }}
            />
            <Button text="Cancel" click={onClose} />
            <Button text="Save" click={showCroppedImage} />
          </Styles.BlockButtons>
        </Styles.Wrap>
      </Styles.Container>
    </Styles.Overlay>
  );
};

export { Crop };
