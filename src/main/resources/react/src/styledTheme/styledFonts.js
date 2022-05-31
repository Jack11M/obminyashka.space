import { createGlobalStyle } from 'styled-components';
import PollywogCyr from '../fonts/Pollywog_Cyr.ttf';

export default createGlobalStyle`

  @import url('https://fonts.googleapis.com/css?family=Roboto:400,400i,700&display=swap&subset=cyrillic');

  @import url('https://fonts.googleapis.com/css2?family=Open+Sans&display=swap');

@font-face {
  font-family: 'Pollywog_Cyr';
  src: url(${PollywogCyr}) format('truetype');
  font-weight: normal;
  font-style: normal;
  font-display: block;
}
`;
