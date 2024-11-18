import { route } from "../../routes/routeConstants";

const routesWithWhiteBG = [route.login, route.signUp];

export const isWhiteBG = (route: string) => routesWithWhiteBG.includes(route);
