import { Icon, Avatar } from '@wolshebnik/obminyashka-components';

import { Button, EllipsisText } from 'components/common';

import * as Styles from './styles';
import { InboxMessage } from './inbox-message';
import { getTranslatedText } from '../local/localization';

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
  return (
    <Styles.Card margin={margin}>
      <Styles.FavoriteMarker isFavorite={isFavorite}>
        {avatar ? (
          <Styles.StylizedAvatar>
            <Avatar width={40} height={40} source={avatar} />
          </Styles.StylizedAvatar>
        ) : (
          <Styles.FavoriteStarWrapper isFavorite={isFavorite}>
            <Styles.FavoriteStar />
          </Styles.FavoriteStarWrapper>
        )}
      </Styles.FavoriteMarker>

      <Styles.DivPicture>
        {typeof picture === 'string' ? (
          <Styles.Picture src={picture} alt="lot" />
        ) : (
          // TODO: check if picture
          picture
        )}
      </Styles.DivPicture>

      <Styles.CardContent>
        <Styles.TextContent>{text}</Styles.TextContent>

        <Styles.LocationIcon>
          <Icon.Location />

          <Styles.CitySpan>
            <EllipsisText>{city}</EllipsisText>
          </Styles.CitySpan>
        </Styles.LocationIcon>

        <Styles.ButtonBlock>
          <Button
            click={clickOnButton}
            width={inboxMessage ? '190px' : '222px'}
            text={getTranslatedText('button.look')}
          />

          {inboxMessage && (
            <InboxMessage inboxMessage={inboxMessage} time="today" />
          )}
        </Styles.ButtonBlock>
      </Styles.CardContent>
    </Styles.Card>
  );
};

export { ProductCard };
