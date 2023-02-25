import { Icon } from '@wolshebnik/obminyashka-components';

import { route } from 'routes/routeConstants';

export const links = [
  {
    url: '',
    icon: <Icon.Activity />,
    textKey: 'panel.myActivity',
    end: true,
  },
  {
    url: route.myProfile,
    icon: <Icon.Profile />,
    textKey: 'panel.myProfile',
  },
  {
    url: route.myFavorite,
    icon: <Icon.Star />,
    textKey: 'panel.myFavorite',
  },
  {
    url: route.mySettings,
    icon: <Icon.Settings />,
    textKey: 'panel.mySettings',
  },
  {
    url: route.exit,
    icon: <Icon.Logout />,
    textKey: 'panel.myExit',
    onClick: true,
  },
];
