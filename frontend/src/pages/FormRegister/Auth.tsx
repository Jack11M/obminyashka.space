import { useState } from "react";
import { Images, Responsive } from "obminyashka-components";

import NavBar from "./nav-bar";
import * as Styles from "./styles";
import { Login } from "./sign-in";
import { SignUp } from "./sign-up";

const Auth = () => {
  const [tab, setTab] = useState<number>(0);
  const isLogin = tab === 0;

  return (
    <Styles.Wrapper>
      <Responsive.Desktop>
        {tab === 0 && (
          <Styles.SunLogin
            alt="sun-login"
            src={Images.sunTransportForChildren}
          />
        )}
      </Responsive.Desktop>

      <Styles.CloudOne alt="cloud-1" src={Images.cloud} />
      <Styles.CloudTwo alt="cloud-2" src={Images.cloud} />
      <Styles.CloudThree alt="cloud-3" src={Images.cloud} />
      <Styles.CloudFour alt="cloud-4" src={Images.cloud} />
      <Styles.CloudFive alt="cloud-5" src={Images.cloud} />
      <Styles.CloudSix alt="cloud-6" src={Images.cloud} />
      <Styles.CloudSeven alt="cloud-7" src={Images.cloud} />
      <Styles.CloudEight alt="cloud-8" src={Images.cloud} />
      <Styles.CloudNine alt="cloud-9" src={Images.cloud} />

      <Styles.FormWrapper variant={tab}>
        <NavBar tab={tab} setTab={setTab} />
        {isLogin && <Login setTab={setTab} />}
        {!isLogin && <SignUp setTab={setTab} />}
      </Styles.FormWrapper>
    </Styles.Wrapper>
  );
};

export { Auth };
