import { route } from "../../routes/routeConstants";

const routesWithWhiteBG = [route.login, route.signUp, route.confirmation];

export const isWhiteBG = (route: string) => routesWithWhiteBG.includes(route);
