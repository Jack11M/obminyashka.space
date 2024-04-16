/* eslint-disable */
// @ts-nocheck
// TODO: fix typescript
import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { Icon, Title, ProductCard, showMessage } from "obminyashka-components";

import api from "src/REST/Resources";
import { route } from "src/routes/routeConstants";
import { getErrorMessage } from "src/Utils/error";
import { getCity } from "src/Utils/getLocationProperties";
import { getTranslatedText } from "src/components/local/localization";

import * as Styles from "./styles";

const CurrentOffers = () => {
  const navigate = useNavigate();

  const [offers, setOffers] = useState([]);

  useEffect(() => {
    api.home
      .getCurrentOffers()
      .then(({ data }) => {
        if (Array.isArray(data.content)) setOffers(data.content);
      })
      .catch((e) => {
        showMessage.error(getErrorMessage(e));
      });
  }, []);

  const moveToProductPage = (id) => {
    navigate(route.productPage.replace(":id", id));
  };

  return (
    <>
      {offers.length && (
        <Styles.ProductSection>
          <Styles.ProductHeader>
            <Title
              style={{ paddingLeft: "0" }}
              text={getTranslatedText("mainPage.blueText")}
            />
          </Styles.ProductHeader>

          <Styles.ProductListUl>
            {offers.map((offer) => (
              <Styles.ProductListLI key={offer.advertisementId}>
                <ProductCard
                  margin="10px 0"
                  text={offer.title}
                  isFavorite={false}
                  city={getCity(offer.location)}
                  buttonText={getTranslatedText("button.look")}
                  onClick={() => moveToProductPage(offer.advertisementId)}
                  picture={
                    offer.image ? (
                      `data:image/jpeg;base64,${offer.image}`
                    ) : (
                      <Icon.NoPhoto />
                    )
                  }
                />
              </Styles.ProductListLI>
            ))}
          </Styles.ProductListUl>
        </Styles.ProductSection>
      )}
    </>
  );
};

export { CurrentOffers };
