import { NoPhotos } from 'assets/icons';

import { NoPhotoContainer, NoPhotoImg } from './styles';

const NoPhoto = ({ noPhoto }) => (
  <NoPhotoContainer noPhoto={noPhoto}>
    <NoPhotoImg src={NoPhotos} alt="no slide" />
  </NoPhotoContainer>
);
export default NoPhoto;
