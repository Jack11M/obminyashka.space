import { Button, Avatar } from 'components/common';

import SvgStar from './FavoriteMarker';
import InboxMessageSvg from './inboxMessageSvg';
import { getTranslatedText } from '../local/localization';

import {
  Card,
  Picture,
  CitySpan,
  DivPicture,
  ButtonBlock,
  CardContent,
  TextContent,
  LocationIcon,
  FavoriteMarker,
} from './styledForCard';

import './avatarForCard.scss';

const ProductCard = ({
  text,
  city,
  avatar = '',
  picture,
  isFavorite,
  margin = 0,
  inboxMessage,
  clickOnButton,
}) => {
  const avatarValue = avatar === '' || avatar;

  return (
    <Card margin={margin}>
      <FavoriteMarker isFavorite={isFavorite}>
        {avatar ? (
          <Avatar
            whatIsClass="avatar-for-card"
            avatar={avatarValue}
            width="40px"
            height="40px"
          />
        ) : (
          <SvgStar isFavorite={isFavorite} />
        )}
      </FavoriteMarker>

      <DivPicture>
        <Picture src={picture} alt="lot" />
      </DivPicture>

      <CardContent>
        <TextContent>{text}</TextContent>

        <LocationIcon inbox={inboxMessage}>
          <span className="icon-location" />
          <CitySpan>{city}</CitySpan>
        </LocationIcon>
        <ButtonBlock>
          <Button
            click={clickOnButton}
            whatClass=""
            text={getTranslatedText('button.look')}
            width={inboxMessage ? '190px' : '222px'}
          />

          {inboxMessage && <InboxMessageSvg inboxMessage={inboxMessage} />}
        </ButtonBlock>
      </CardContent>
    </Card>
  );
};

export default ProductCard;
