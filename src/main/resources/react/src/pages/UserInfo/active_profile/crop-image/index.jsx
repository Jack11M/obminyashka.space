import { useContext, useState, useEffect } from 'react';
import imageCompression from 'browser-image-compression';

import * as Icon from 'assets/icons';
import { getTranslatedText } from 'components/local';
import { Avatar, Crop, ModalContext } from 'components/common';
import { constants, convertToMB, options, useDelay } from 'Utils';

import * as Styles from './styles';

const CropImage = ({ avatarImage }) => {
  const { openModal } = useContext(ModalContext);

  const [openCrop, setOpenCrop] = useState(false);
  const [image, setImage] = useState(avatarImage);
  const [croppedImage, setCroppedImage] = useState(null);
  const [showIcon, setShowIcon] = useDelay(300);

  useEffect(() => {
    if (avatarImage) {
      setImage(`data:image/jpeg;base64,${avatarImage}`);
    }
  }, [avatarImage]);

  const handleOpenCrop = () => {
    if (image) {
      setCroppedImage(image);
      setOpenCrop(true);
    }
  };
  const changeFile = async (event) => {
    const file = event.target.files[0];
    const { value, valueString } = convertToMB(file.size);

    if (value >= constants.MAX_SIZE_PHOTO && valueString.includes('MB')) {
      openModal({
        title: getTranslatedText('popup.errorTitle'),
        children: (
          <p style={{ textAlign: 'center' }}>
            {getTranslatedText('popup.sizeFile')}
            &nbsp;
            {valueString}
            <br />
            &nbsp;
            {getTranslatedText('popup.selectFile')}
          </p>
        ),
      });
      event.target.value = null;
      return;
    }

    if (!file?.type.match('image') || file.type.match('image/svg')) {
      openModal({
        title: getTranslatedText('popup.errorTitle'),
        children: (
          <p style={{ textAlign: 'center' }}>
            {getTranslatedText('popup.pictureSelection')} <br />( jpg, jpeg,
            png, gif ).
          </p>
        ),
      });
      event.target.value = null;
      return;
    }

    const compressedFile = await imageCompression(file, options);

    const reader = new FileReader();
    reader.readAsDataURL(compressedFile);
    reader.onload = async ({ target }) => {
      if (target.readyState === 2) {
        setCroppedImage(target.result);
        setOpenCrop(true);
      }
    };
  };

  return (
    <>
      <Styles.WrapAvatar
        hasImage={!!image}
        showIcon={showIcon}
        onClick={handleOpenCrop}
        onMouseEnter={() => setShowIcon(true)}
        onMouseLeave={() => setShowIcon(false)}
      >
        <Avatar width={135} height={135} source={image} />

        {showIcon && (
          <Styles.WrapCropSvg>
            <Icon.Camera />
          </Styles.WrapCropSvg>
        )}

        {!openCrop && !image && (
          <Styles.InputFile
            type="file"
            name="file"
            onChange={changeFile}
            accept=".png, .jpg, .jpeg, .gif"
          />
        )}
      </Styles.WrapAvatar>

      {openCrop && (
        <Crop
          disabled={!image}
          setImage={setImage}
          image={croppedImage}
          setCroppedImage={setCroppedImage}
          onClose={() => setOpenCrop(false)}
        />
      )}
    </>
  );
};

export { CropImage };
