import { useState } from 'react';

import { ImgPhoto, WrapImage, SpanClose } from './styles';

const ImagePhoto = ({
  url,
  index,
  onDrop,
  onDragEnd,
  onDragOver,
  removeImage,
  onDragStart,
  onDragLeave,
}) => {
  const [isRemove, setIsRemove] = useState(false);

  const delayRemove = (event, idx) => {
    setIsRemove(true);
    const timeoutForDelete = setTimeout(() => {
      setIsRemove(false);
      removeImage(event, idx);
      clearTimeout(timeoutForDelete);
    }, 300);
  };

  return (
    <WrapImage
      draggable
      onDrop={onDrop}
      isRemove={isRemove}
      onDragEnd={onDragEnd}
      onDragOver={onDragOver}
      onDragStart={onDragStart}
      onDragLeave={onDragLeave}
    >
      <ImgPhoto src={url} alt="photo" />
      <SpanClose onClick={(event) => delayRemove(event, index)} />
    </WrapImage>
  );
};

export { ImagePhoto };
