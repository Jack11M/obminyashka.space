import { route } from 'src/routes/routeConstants';

export const goTo = (home, navigate, deactivateError) => {
  if (deactivateError) deactivateError(false);

  if (home === 'home') navigate(route.home);
  else navigate(-1);
};
