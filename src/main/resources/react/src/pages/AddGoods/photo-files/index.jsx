import { useContext } from 'react';
import imageCompression from 'browser-image-compression';

import { ModalContext } from 'components/common';
import { getTranslatedText } from 'components/local/localization';
import { constants, convertToMB, isRightExtension, options } from 'Utils';

import { ImagePhoto } from './image-photo';
import { AddFileInput } from './add-file-input';

import * as Styles from '../styles';

const PhotoFiles = ({
  imageFiles,
  preViewImage,
  setImageFiles,
  setPreViewImage,
  currentIndexImage,
  setCurrentIndexImage,
}) => {
  const { openModal } = useContext(ModalContext);

  const filesAddHandler = async (event, dropFiles = null) => {
    event.preventDefault();

    const files = Array.from(dropFiles || event.target.files);

    files.forEach((file, index, iterableArray) => {
      const notAbilityToDownload =
        10 - imageFiles.length - iterableArray.length < 0;

      const foundSameFile = imageFiles.some(
        (image) => image.name === file.name
      );

      if (foundSameFile) {
        openModal({
          title: getTranslatedText('popup.errorTitle'),
          children: (
            <p style={{ textAlign: 'center' }}>
              {getTranslatedText('popup.addedFile')}
            </p>
          ),
        });
        return;
      }

      if (isRightExtension(file.type)) {
        openModal({
          title: getTranslatedText('popup.errorTitle'),
          children: (
            <p style={{ textAlign: 'center' }}>
              {getTranslatedText('popup.pictureSelection')} <br />( jpg, jpeg,
              png, gif ).
            </p>
          ),
        });
        return;
      }

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
        return;
      }

      if (notAbilityToDownload) {
        openModal({
          title: getTranslatedText('popup.errorTitle'),
          children: (
            <p style={{ textAlign: 'center' }}>
              {getTranslatedText('popup.noSaveMore')}
            </p>
          ),
        });
        return;
      }

      const reader = new FileReader();
      reader.readAsDataURL(file);
      reader.onload = async ({ target }) => {
        if (target.readyState === 2) {
          const compressedFile = await imageCompression(file, options);
          setPreViewImage((prev) => [...prev, target.result]);
          setImageFiles((prev) => [...prev, compressedFile]);
          event.target.value = null;
        }
      };
    });
  };

  const removeImage = (event, index) => {
    event.preventDefault();

    const newImageFiles = [...imageFiles];
    const newPreViewImage = [...preViewImage];
    newPreViewImage.splice(index, 1);
    newImageFiles.splice(index, 1);
    setImageFiles(newImageFiles);
    setPreViewImage(newPreViewImage);
  };

  const dragStartHandler = (_, index) => {
    setCurrentIndexImage(index);
  };

  const dragEndHandler = (e) => {
    e.target.style.background = 'white';
  };

  const dragOverHandler = (e) => {
    e.preventDefault();
    e.target.style.background = 'lightgrey';
  };

  const changeStateForImagesWhenDrop = (
    index,
    processedArray,
    setProcessedArray
  ) => {
    const newPrevArr = [...processedArray];
    const underPrevImage = newPrevArr[index];
    const currentPrevImage = newPrevArr[currentIndexImage];
    newPrevArr[currentIndexImage] = underPrevImage;
    newPrevArr[index] = currentPrevImage;
    setProcessedArray(newPrevArr);
  };

  const dropHandler = (e, index) => {
    e.preventDefault();
    e.target.style.background = 'white';
    changeStateForImagesWhenDrop(index, preViewImage, setPreViewImage);
    changeStateForImagesWhenDrop(index, imageFiles, setImageFiles);
  };

  return (
    <Styles.WrapFiles>
      <Styles.FileTittle>
        {getTranslatedText('addAdv.uploadDescription')}&nbsp;
        <Styles.Star>*</Styles.Star>
      </Styles.FileTittle>

      <p>{getTranslatedText('addAdv.firstUploadDescription')}</p>

      <Styles.FileDescription>
        {getTranslatedText('addAdv.photosUploaded')}
        &nbsp;
        {imageFiles.length}
        &nbsp;
        {getTranslatedText('addAdv.from')}
        &nbsp;10
      </Styles.FileDescription>

      <Styles.WrapperFile>
        {preViewImage.map((url, index) => (
          <ImagePhoto
            url={url}
            index={index}
            key={String(`${index}`)}
            removeImage={removeImage}
            onDragEnd={(e) => dragEndHandler(e)}
            onDrop={(e) => dropHandler(e, index)}
            onDragLeave={(e) => dragEndHandler(e)}
            onDragOver={(e) => dragOverHandler(e)}
            onDragStart={(e) => dragStartHandler(e, index)}
          />
        ))}

        {imageFiles.length < 10 && <AddFileInput onChange={filesAddHandler} />}
      </Styles.WrapperFile>
    </Styles.WrapFiles>
  );
};
export { PhotoFiles };
