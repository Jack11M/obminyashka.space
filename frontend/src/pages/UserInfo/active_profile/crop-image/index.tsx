/* eslint-disable */
// @ts-nocheck
// TODO: fix typescript
import { useContext, useEffect, useState } from 'react';
import { Avatar, Icon } from 'obminyashka-components';
import imageCompression from 'browser-image-compression';

import { getTranslatedText } from 'src/components/local';
import { Crop, ModalContext } from 'src/components/common';
import { options, useDelay, convertToUnitOfDigitalInformation, isImageExtensionSupported } from 'src/Utils';

import * as Styles from './styles';
import { MAX_SIZE_PHOTO } from "src/Utils/convertToUnitOfDigitalInformation.ts";

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
    const { value, valueString } = convertToUnitOfDigitalInformation(file.size);

    if (value >= MAX_SIZE_PHOTO && valueString.includes('MB')) {
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

    if (!isImageExtensionSupported(file?.type)) {
      openModal({
        title: getTranslatedText('popup.errorTitle'),
        children: (
          <p style={{ textAlign: 'center' }}>
            {getTranslatedText('popup.pictureSelection')} <br />( jpg, jpeg, png, gif ).
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
            type='file'
            name='file'
            onChange={changeFile}
            accept='.png, .jpg, .jpeg, .gif'
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
