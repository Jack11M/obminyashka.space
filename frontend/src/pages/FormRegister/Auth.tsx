import { useState } from "react";
import { Images, Responsive } from "obminyashka-components";

import * as Styles from "./styles";
import NavBar from "./nav-bar";

const Auth = () => {
  const [variant, setVariant] = useState<number>(0);

  return (
    <Styles.Main>
      <Responsive.Desktop>
        {variant === 0 && (
          <Styles.SunLogin
            alt="sun-login"
            src={Images.sunTransportForChildren}
          />
        )}
      </Responsive.Desktop>

      <Styles.Form variant={variant}>
        {/* <Responsive.Desktop>
          {variant === 1 && (
            <Styles.SunRegistration
              src={Images.sunToys}
              alt="sun-registration"
            />
          )}
        </Responsive.Desktop> */}

        {/* <Styles.NavBarContainer> */}
        <NavBar setVariant={setVariant} />
        {/* </Styles.NavBarContainer> */}
      </Styles.Form>
    </Styles.Main>
  );
};

export { Auth };
