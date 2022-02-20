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

  const delayRemove = (event, index) => {
    setIsRemove(true);
    const timeoutForDelete = setTimeout(() => {
      setIsRemove(false);
      removeImage(event, index);
      clearTimeout(timeoutForDelete);
    }, 300);
  };
  return (
    <WrapImage
      onDrop={onDrop}
      draggable={true}
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
