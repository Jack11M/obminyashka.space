import { ButtonNew, Icon } from "obminyashka-components";

import { getTranslatedText } from "src/components/local";

import * as Styles from "./styles";

interface IButtonsWrapper {
  register: boolean;
}

export const ButtonsWrapper = ({ register }: IButtonsWrapper) => {
  return (
    <Styles.WrapperButtons>
      <ButtonNew
        type="submit"
        animated={true}
        colorType="blue"
        styleType="default"
        text={
          register
            ? getTranslatedText("auth.signUp")
            : getTranslatedText("button.enter")
        }
      />

      <Styles.FirstText>{getTranslatedText("auth.or")}</Styles.FirstText>

      <Styles.SecondText>
        {getTranslatedText("auth.socialNetwork")}
      </Styles.SecondText>

      <Styles.ButtonsRegistration>
        <ButtonNew
          square={true}
          colorType="blue"
          styleType="outline"
          icon={<Icon.FbRegistration />}
        />
        <ButtonNew
          square={true}
          colorType="blue"
          styleType="outline"
          icon={<Icon.AppleRegistration />}
        />
        <ButtonNew
          square={true}
          colorType="blue"
          styleType="outline"
          icon={<Icon.GoogleRegistration />}
          onClick={() => window.location.assign("/oauth2/authorization/google")}
        />
      </Styles.ButtonsRegistration>
    </Styles.WrapperButtons>
  );
};
