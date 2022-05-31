import { route } from 'routes/routeConstants';

export const links = [
  {
    url: '',
    classname: 'icon-activity',
    textKey: 'panel.myActivity',
    end: true,
  },
  {
    url: route.myProfile,
    classname: 'icon-profile',
    textKey: 'panel.myProfile',
  },
  {
    url: route.myFavorite,
    classname: 'icon-star',
    textKey: 'panel.myFavorite',
  },
  {
    url: route.mySettings,
    classname: 'icon-settings',
    textKey: 'panel.mySettings',
  },
  {
    url: route.exit,
    classname: 'icon-logout',
    textKey: 'panel.myExit',
    onClick: true,
  },
];
