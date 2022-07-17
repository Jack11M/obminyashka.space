import { useSelector } from 'react-redux';

import { getLang } from 'store/auth/slice';
import { Avatar } from 'components/common/avatar';
import { Button } from 'components/common/buttons';

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
  picture,
  isFavorite,
  margin = 0,
  avatar = '',
  inboxMessage,
  clickOnButton,
}) => {
  const lang = useSelector(getLang);
  const avatarValue = avatar === '' || avatar;

  return (
    <Card margin={margin}>
      <FavoriteMarker isFavorite={isFavorite}>
        {avatar ? (
          <Avatar
            width="40px"
            height="40px"
            avatar={avatarValue}
            whatIsClass="avatar-for-card"
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
            whatClass=""
            click={clickOnButton}
            width={inboxMessage ? '190px' : '222px'}
            text={getTranslatedText('button.look', lang)}
          />

          {inboxMessage && <InboxMessageSvg inboxMessage={inboxMessage} />}
        </ButtonBlock>
      </CardContent>
    </Card>
  );
};

export default ProductCard;
