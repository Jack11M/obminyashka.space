import styled from 'styled-components';

export const Card = styled.div`
  position: relative;
  width: 290px;
  height: 419px;
  border-radius: 20px;
  border: 2px dotted #c4c4c4;
  background-color: #fff;
  margin: ${({ margin }) => margin || '10px 14px'};
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
  background-color: ${({ isFavorite = false }) =>
    isFavorite ? '#FEE200' : '#A0A0A0'};

  &:hover {
    cursor: pointer;
    background-color: ${({ isFavorite = false }) =>
      isFavorite ? 'hsl(53, 100%, 48%)' : 'hsl(0, 0%, 59%)'};
  }

  &:active {
    cursor: pointer;
    background-color: ${({ isFavorite = false }) =>
      isFavorite ? 'hsl(53, 100%, 44%)' : 'hsl(0, 0%, 59%)'};
  }
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
  color: ${({ theme: { colors } }) => colors['black-color-text']};
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
  color: ${({ theme: { colors } }) => colors['btn-blue-hover']};
  font-size: 14px;
  margin-left: 9px;
`;

export const ButtonBlock = styled.div`
  display: flex;
`;

export const InboxSvg = styled.svg`
  transition: background-color 0.3s ease;
`;

export const InboxDiv = styled.div`
  position: relative;
  margin-left: 16px;

  &:hover {
    cursor: pointer;
    ${InboxSvg}:hover {
      cursor: pointer;
      path {
        fill: hsl(116, 60%, 45%);
      }
    }

    ${InboxSvg}:active {
      cursor: pointer;
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
