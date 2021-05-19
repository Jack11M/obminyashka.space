import React from 'react';
import noPhotos from '../../../img/showAdv/noPhoto.svg'

const NoPhoto = ( { noPhoto = 'noPhoto' , noPhotoImg = 'noPhotoImg' } ) => {
  return (
    <div className = { noPhoto }>
      <img src={noPhotos} className = { noPhotoImg } alt='no slide'/>
    </div>
  );
};
export default NoPhoto;
