import { useState } from 'react';

import { WrapImage, SpanClose, ImgPhoto } from './styles';

const ImagePhoto = ({
  url,
  index,
  removeImage,
  onDrop,
  onDragEnd,
  onDragOver,
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
      isRemove={isRemove}
      draggable={true}
      onDragStart={onDragStart}
      onDragLeave={onDragLeave}
      onDragEnd={onDragEnd}
      onDragOver={onDragOver}
      onDrop={onDrop}
    >
      <ImgPhoto src={url} alt="photo" />
      <SpanClose onClick={(event) => delayRemove(event, index)} />
    </WrapImage>
  );
};

export { ImagePhoto };
