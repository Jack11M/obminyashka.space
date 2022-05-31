import noPhotos from '../../../assets/img/showAdv/noPhoto.svg';

const NoPhoto = ({ noPhoto = 'noPhoto', noPhotoImg = 'noPhotoImg' }) => (
  <div className={noPhoto}>
    <img src={noPhotos} className={noPhotoImg} alt="no slide" />
  </div>
);
export default NoPhoto;
