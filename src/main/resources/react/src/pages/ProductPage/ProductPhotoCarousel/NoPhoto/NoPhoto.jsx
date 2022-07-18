import noPhotos from '../../../../assets/img/showAdv/noPhoto.svg';

import { NoPhotoContainer, NoPhotoImg } from './styles';

const NoPhoto = ({ noPhoto }) => (
  <NoPhotoContainer noPhoto={noPhoto}>
    <NoPhotoImg src={noPhotos} alt="no slide" />
  </NoPhotoContainer>
);
export default NoPhoto;
