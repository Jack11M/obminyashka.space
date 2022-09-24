/* eslint-disable indent */
import styled, { css } from 'styled-components';

import * as Icon from 'assets/icons';

export const Card = styled.div`
  position: relative;
  width: 290px;
  height: 419px;
  border-radius: 20px;
  border: 2px dotted #c4c4c4;
  background-color: #fff;
  margin: ${({ margin }) => margin || 0};
  -webkit-box-shadow: 0 7px 10px 0 rgba(48, 50, 50, 0.5);
  -moz-box-shadow: 0 7px 10px 0 rgba(48, 50, 50, 0.5);
  box-shadow: 0 7px 10px 0 rgba(48, 50, 50, 0.5);
`;

export const FavoriteMarker = styled.span`
  position: absolute;
  left: 50%;
  top: 0;
  width: 50px;
  height: 50px;
  border-radius: 0 0 25px 25px;
  transform: translate(-50%);
  transition: background-color 0.3s ease;

  ${({ isFavorite = false }) => css`
    background-color: ${isFavorite ? '#FEE200' : '#A0A0A0'};

    &:hover {
      cursor: pointer;
      background-color: ${isFavorite
        ? 'hsl(53, 100%, 48%)'
        : 'hsl(0, 0%, 59%)'};
    }

    &:active {
      cursor: pointer;
      background-color: ${isFavorite
        ? 'hsl(53, 100%, 44%)'
        : 'hsl(0, 0%, 59%)'};
    }
  `}
`;

export const DivPicture = styled.div`
  margin-bottom: 10px;
  width: 286px;
  height: 242px;
`;

export const Picture = styled.img`
  font-size: 0;
  border-radius: 19px 19px 0 0;
  object-fit: cover;
  width: 100%;
  height: 100%;
`;

export const CardContent = styled.div`
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
`;

export const TextContent = styled.h6`
  height: 37px;
  font-size: 16px;
  line-height: 19px;
  color: ${({ theme }) => theme.colors.blackColorText};
  margin: -5px 44px 10px;
  text-align: center;

  -webkit-line-clamp: 2;
  display: -webkit-box;
  -webkit-box-orient: vertical;
  overflow: hidden;
  word-break: break-word;
`;

export const LocationIcon = styled.div`
  margin-bottom: ${({ inbox }) => (inbox ? '36px' : '23px')};
`;
export const CitySpan = styled.span`
  color: ${({ theme: { colors } }) => colors.btnBlueHover};
  font-size: 14px;
  margin-left: 9px;
`;

export const ButtonBlock = styled.div`
  display: flex;
`;

export const InboxDiv = styled.div`
  position: relative;
  margin-left: 16px;
  cursor: pointer;

  path {
    transition: fill 0.3s ease;
  }

  &:hover {
    svg {
      path {
        fill: hsl(116, 60%, 45%);
      }
    }
  }

  &:active {
    svg {
      path {
        fill: hsl(116, 60%, 41%);
      }
    }
  }
`;

export const InboxSpan = styled.span`
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -62%);
  font-family: 'Open Sans', sans-serif;

  font-size: 16px;
  font-style: normal;
  font-weight: 800;
  color: #fff;
`;

export const StylizedAvatar = styled.div`
  position: absolute;
  left: 50%;
  top: 4px;
  border-radius: 25px;
  transform: translate(-50%);
`;

export const FavoriteStar = styled(Icon.FavoriteSarSvg)`
  display: inline-block;
  width: 100%;
  margin: 11px auto;
`;

export const FavoriteStarWrapper = styled.div`
  path {
    fill: ${({ isFavorite }) => (isFavorite ? '#12b6ed' : '#fff')};
  }
`;
