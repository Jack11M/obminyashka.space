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
  const [adv, setAdv] = useState({});
  const [pageSize, setPageSize] = useState(1);
  const [showMore, setShowMore] = useState([]);
  const { search, setIsFetch } = useContext(SearchContext);
  const [searchParams, setSearchParams] = useSearchParams();
  const [disabledPages, setDisabledPages] = useState<number[]>([]);

  const searchResults = search || searchParams.get("search");

  const getAdv = async (page: number) => {
    const currentPage = page ?? 1;

    const requestData = {
      page: currentPage - 1,
      size: pageSize,
      ...(searchResults && { keyword: searchResults }),
      ...Object.fromEntries(
        [...searchParams]
          .filter(([key]) => key !== "search")
          .map(([key, value]) => [key, JSON.parse(value)])
      ),
    };

    try {
      const response = await api.search.postFilter(requestData);
      setAdv(response);
    } catch (err) {
      console.error(err);
    } finally {
      setIsFetch(false);
    }
  };

  const moveToProductPage = (id) => {
    navigate(route.productPage.replace(":id", id));
  };

  const handleShowMore = () => {
    setShowMore([...showMore, ...adv.content]);
    setDisabledPages([...disabledPages, adv.number + 1]);
    const nextPage = adv.number + 1;
    getAdv(nextPage + 1);
  };

  const handleClear = (event) => {
    const target = event.target;
    if (target.closest(".rc-pagination-item")) {
      setShowMore([]);
      setDisabledPages([]);
    }
  };

  useEffect(() => {
    const paginationItems = document.querySelectorAll(".rc-pagination-item");
    paginationItems.forEach((item, index) => {
      if (disabledPages.includes(index + 1)) {
        item.classList.add("disabled");
      } else {
        item.classList.remove("disabled");
      }
    });
  }, [adv.number]);

  useEffect(() => {
    getAdv(adv.number + 1);
  }, [pageSize]);

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

        <Styles.PaginationContainer onClick={handleClear}>
          <Title
            style={{ paddingLeft: "0", marginBottom: "50px" }}
            text={getTranslatedText("filterPage.searchResults")}
          />

          {adv.content && (
            <PagePagination
              onChange={getAdv}
              current={adv.number + 1}
              pageSize={adv?.size || 1}
              total={adv.totalElements}
              handleShowMore={handleShowMore}
              text={getTranslatedText("paginationBtnText.showMore")}
            >
              {adv.content?.length > 0 &&
                [
                  ...showMore.filter(
                    (item) =>
                      !adv.content.find(
                        (contentItem) =>
                          contentItem.advertisementId === item.advertisementId
                      )
                  ),
                  ...adv.content,
                ].map((item) => (
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
        </Styles.PaginationContainer>
      </Styles.SearchingContent>
    </Styles.SearchingResults>
  );
};

export { SearchResults };
