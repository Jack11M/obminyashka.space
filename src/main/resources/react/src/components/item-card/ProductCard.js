import React from 'react';
import { useSelector } from 'react-redux';

import { Avatar } from 'components/common/avatar';
import Button from 'components/common/buttons/button/Button';

import SvgStar from './FavoriteMarker';
import InboxMessageSvg from './inboxMessageSvg';
import { getTranslatedText } from '../local/localization';

import {
  ButtonBlock,
  Card,
  CardContent,
  CitySpan,
  DivPicture,
  FavoriteMarker,
  LocationIcon,
  Picture,
  TextContent,
} from './styledForCard';

import './avatarForCard.scss';

const ProductCard = (props) => {
  const { lang } = useSelector((state) => state.auth);
  const avatar = props.avatar === '' || props.avatar;

  return (
    <Card margin={props.margin}>
      <FavoriteMarker isFavorite={props.isFavorite}>
        {avatar ? (
          <Avatar
            whatIsClass={'avatar-for-card'}
            avatar={props.avatar}
            width={'40px'}
            height={'40px'}
          />
        ) : (
          <SvgStar isFavorite={props.isFavorite} />
        )}
      </FavoriteMarker>

      <DivPicture>
        <Picture src={props.picture} alt="lot" className="" />
      </DivPicture>

      <CardContent>
        <TextContent>{props.text}</TextContent>

        <LocationIcon inbox={props.inboxMessage}>
          <span className="icon-location" />
          <CitySpan>{props.city}</CitySpan>
        </LocationIcon>
        <ButtonBlock>
          <Button
            click={props.clickOnButton}
            whatClass=""
            text={getTranslatedText('button.look', lang)}
            width={props.inboxMessage ? '190px' : '222px'}
          />

          {props.inboxMessage && (
            <InboxMessageSvg inboxMessage={props.inboxMessage} />
          )}
        </ButtonBlock>
      </CardContent>
    </Card>
  );
};

export default ProductCard;
