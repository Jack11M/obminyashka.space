import { Button } from 'components/common';
import { Avatar } from 'components/common/avatar';

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
        <Styles.Picture src={picture} alt="lot" />
      </Styles.DivPicture>

      <Styles.CardContent>
        <Styles.TextContent>{text}</Styles.TextContent>

        <Styles.LocationIcon inbox={inboxMessage}>
          <span className="icon-location" />

          <Styles.CitySpan>{city}</Styles.CitySpan>
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
