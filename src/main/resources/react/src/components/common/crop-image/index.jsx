import { useCallback, useState } from 'react';
import Cropper from 'react-easy-crop';

import * as Styles from './styles';

const CropImage = () => {
  const [crop, setCrop] = useState({ x: 0, y: 0 });
  const [zoom, setZoom] = useState(1);
  const onCropComplete = useCallback((croppedArea, croppedAreaPixels) => {
    console.log(croppedArea, croppedAreaPixels);
  }, []);

  return (
    <Styles.Container>
      <Cropper
        image="https://img.huffingtonpost.com/asset/5ab4d4ac2000007d06eb2c56.jpeg?cache=sih0jwle4e&ops=1910_1000"
        crop={crop}
        zoom={zoom}
        aspect={1}
        cropShape="round"
        objectFit="vertical-cover"
        onCropChange={setCrop}
        onCropComplete={onCropComplete}
        onZoomChange={setZoom}
      />
    </Styles.Container>
  );
};

export { CropImage };
