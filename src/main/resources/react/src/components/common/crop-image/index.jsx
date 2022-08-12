import { useCallback, useState } from 'react';
import Cropper from 'react-easy-crop';

import { Button } from 'components/common';

import * as Styles from './styles';

const CropImage = () => {
  const [crop, setCrop] = useState({ x: 0, y: 0 });
  const [zoom, setZoom] = useState(1);
  const onCropComplete = useCallback((croppedArea, croppedAreaPixels) => {
    console.log(croppedArea, croppedAreaPixels);
  }, []);

  return (
    <Styles.Overlay>
      <Styles.Container>
        <Styles.Wrap>
          <Styles.Title>Add photo</Styles.Title>

          <Styles.BlockCrop>
            <Cropper
              image="https://img.huffingtonpost.com/asset/5ab4d4ac2000007d06eb2c56.jpeg?cache=sih0jwle4e&ops=1910_1000"
              aspect={1}
              crop={crop}
              zoom={zoom}
              cropShape="round"
              onZoomChange={setZoom}
              onCropChange={setCrop}
              objectFit="vertical-cover"
              onCropComplete={onCropComplete}
            />
          </Styles.BlockCrop>

          <Button text="Save" />
        </Styles.Wrap>
      </Styles.Container>
    </Styles.Overlay>
  );
};

export { CropImage };
