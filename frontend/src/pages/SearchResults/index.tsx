/* eslint-disable */
// @ts-nocheck
// TODO: fix typescript
import { useContext, useEffect, useState } from "react";
import { useNavigate, useSearchParams } from "react-router-dom";
import {
  Title,
  showMessage,
  ProductCard,
  PagePagination,
} from "obminyashka-components";

import api from "src/REST/Resources";
import { route } from "src/routes/routeConstants";
import { getCity } from "src/Utils/getLocationProperties";
import { Filtration, SearchContext } from "src/components/common";
import { getTranslatedText } from "src/components/local/localization";

import * as Styles from "./styles";

const SearchResults = () => {
  const navigate = useNavigate();
  const [searchParams, setSearchParams] = useSearchParams();
  const { search, setIsFetch } = useContext(SearchContext);
  const [adv, setAdv] = useState({});

  const searchResults = search || searchParams.get("search");

  const getAdv = async (page: number) => {
    const currentPage = page ?? 1;
    const requestData: { [key: string]: string[] | number[] | string } = {};

    requestData.page = currentPage - 1;

    if (searchResults) {
      requestData.keyword = searchResults;
    }

    searchParams.forEach((value, key) => {
      key === "search" ? "" : (requestData[key] = JSON.parse(value));
    });

    try {
      const response = await api.search.postFilter(requestData);
      setAdv(response);
    } catch (err) {
      console.error(err);
    } finally {
      setIsFetch(false);
    }
  };

  useEffect(() => {
    getAdv();
  }, []);

  const moveToProductPage = (id) => {
    navigate(route.productPage.replace(":id", id));
  };

  return (
    <Styles.SearchingResults>
      <Styles.SearchingContent>
        <Styles.FilterContainer>
          <Styles.BreadCrumbs>
            {getTranslatedText("filterPage.home")}
            <Styles.Span>
              {getTranslatedText("filterPage.searchResults")}
            </Styles.Span>
          </Styles.BreadCrumbs>

          <Filtration />
        </Styles.FilterContainer>

        <div>
          <Title text={getTranslatedText("filterPage.searchResults")} />

          {adv.content && (
            <PagePagination
              onChange={getAdv}
              current={adv.number + 1}
              pageSize={adv?.size || 1}
              total={adv.totalElements}
            >
              {adv.content?.length > 0 &&
                adv.content.map((item) => (
                  <ProductCard
                    text={item.title}
                    key={item.advertisementId}
                    city={getCity(item.location)}
                    buttonText={getTranslatedText("button.look")}
                    picture={`data:image/jpeg;base64,${item.image}`}
                    onClick={() => moveToProductPage(item.advertisementId)}
                  />
                ))}
            </PagePagination>
          )}
        </div>
      </Styles.SearchingContent>
    </Styles.SearchingResults>
  );
};

export { SearchResults };
