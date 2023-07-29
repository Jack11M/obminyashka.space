import { getTranslatedText } from "src/components/local/localization";

import Tabs from "../tabs";
import { Login } from "../sign-in";
import { SignUp } from "../sign-up";

type Props = {
  setVariant: (index: number) => void;
};

const NavBar: React.FC<Props> = ({ setVariant }) => (
  <Tabs setVariant={setVariant}>
    <span title={`${getTranslatedText("auth.login")}`}>
      <Login />
    </span>

    <span title={`${getTranslatedText("auth.signUp")}`}>
      <SignUp />
    </span>
  </Tabs>
);

export default NavBar;
