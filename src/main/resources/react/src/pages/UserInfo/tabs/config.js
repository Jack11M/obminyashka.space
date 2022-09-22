import { route } from 'routes/routeConstants';

import * as Icon from 'assets/icons';

export const links = [
  {
    url: '',
    icon: <Icon.ActivitySvg />,
    textKey: 'panel.myActivity',
    end: true,
  },
  {
    url: route.myProfile,
    icon: <Icon.ProfileSvg />,
    textKey: 'panel.myProfile',
  },
  {
    url: route.myFavorite,
    icon: <Icon.StarSvg />,
    textKey: 'panel.myFavorite',
  },
  {
    url: route.mySettings,
    icon: <Icon.SettingsSvg />,
    textKey: 'panel.mySettings',
  },
  {
    url: route.exit,
    icon: <Icon.LogoutSvg />,
    textKey: 'panel.myExit',
    onClick: true,
  },
];
