import { getTranslatedText } from "src/components/local/localization";

import * as Styles from "./styles";
interface INavBar {
  tab: number;
  setTab: (num: number) => void;
}

const NavBar = ({ tab, setTab }: INavBar) => {
  return (
    <Styles.NavBarWrapper>
      <Styles.Tab
        className={tab === 0 ? "focus" : ""}
        onClick={() => setTab(0)}
      >
        {getTranslatedText("button.enter")}
      </Styles.Tab>
      <Styles.Tab
        className={tab === 1 ? "focus" : ""}
        onClick={() => setTab(1)}
      >
        {getTranslatedText("auth.signUp")}
      </Styles.Tab>
    </Styles.NavBarWrapper>
  );
};

export default NavBar;
