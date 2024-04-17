/* eslint-disable */
// @ts-nocheck
// TODO: fix typescript
import { useContext, useEffect, useState } from "react";
import { useNavigate, useSearchParams } from "react-router-dom";
import {
  Icon,
  Title,
  ButtonNew,
  Responsive,
  showMessage,
  ProductCard,
  PagePagination,
} from "obminyashka-components";

import api from "src/REST/Resources";
import { route } from "src/routes/routeConstants";
import { Modal } from "src/pages/SearchResults/modal";
import { getCity } from "src/Utils/getLocationProperties";
import { Filtration, SearchContext } from "src/components/common";
import { getTranslatedText } from "src/components/local/localization";

import * as Styles from "./styles";

const SearchResults = () => {
  const navigate = useNavigate();
  const [adv, setAdv] = useState({});
  const [showMore, setShowMore] = useState([]);
  const [dataRequest, setDataRequest] = useState({});
  const [isModal, setIsModal] = useState<boolean>(false);
  const [isSubmit, setIsSubmit] = useState<boolean>(true);
  const { search, setIsFetch } = useContext(SearchContext);
  const [searchParams, setSearchParams] = useSearchParams();
  const [isLoading, setIsLoading] = useState<boolean>(false);
  const [disabledPages, setDisabledPages] = useState<number[]>([]);

  const searchResults = search || searchParams.get("search");

  const getAdv = async (page: number) => {
    const currentPage = page ?? 1;
    let requestData = {};

    if (isSubmit) {
      requestData = {
        page: currentPage - 1,
        // size: 1,
        ...(searchResults && { keyword: searchResults }),
        ...Object.fromEntries(
          [...searchParams]
            .filter(([key]) => key !== "search")
            .map(([key, value]) => [key, JSON.parse(value)])
        ),
      };

      setDataRequest(requestData);
    }

    if (!isSubmit) {
      requestData = dataRequest;
      requestData.page = currentPage - 1;
      // requestData.size = 1;
    }

    try {
      setIsLoading(true);
      const response = await api.search.postFilter(requestData);
      setAdv(response);
    } catch (err) {
      console.error(err);
    } finally {
      setIsFetch(false);
      setIsSubmit(false);
      setIsLoading(false);
    }
  };

  const moveToProductPage = (id) => {
    navigate(route.productPage.replace(":id", id));
  };

  const handleShowMore = () => {
    const nextPage = adv.number + 1;
    setShowMore([...showMore, ...adv.content]);
    setDisabledPages([...disabledPages, adv.number + 1]);

    if (nextPage !== adv.totalPages) {
      getAdv(nextPage + 1);
    }
  };

  const handleClear = (event) => {
    const target = event.target;

    if (
      target.closest(".rc-pagination-item") ||
      target.closest(".rc-pagination-next") ||
      target.closest(".rc-pagination-prev") ||
      target.closest(".rc-pagination-jump-prev") ||
      target.closest(".rc-pagination-jump-next")
    ) {
      setShowMore([]);
      setDisabledPages([]);
    }

    if (
      target.closest(".rc-pagination-next") ||
      target.closest(".rc-pagination-jump-next")
    ) {
      if (disabledPages.length && adv.totalPages === adv.number + 1) {
        getAdv(adv.totalPages);
        return;
      }

      if (disabledPages.length) {
        const nextPage = adv.number + 1;
        getAdv(nextPage + 1);
      }
    }

    if (
      target.closest(".rc-pagination-prev") ||
      target.closest(".rc-pagination-jump-prev")
    ) {
      if (disabledPages.length) {
        setTimeout(() => {
          if (disabledPages[0] !== 1) {
            getAdv(disabledPages[0] - 1);
          } else {
            getAdv(1);
          }
        }, 0);
      }
    }
  };

  const submit = () => {
    setIsModal(false);
    setIsSubmit(true);
  };

  useEffect(() => {
    if (isModal) {
      document.body.style.overflow = "hidden";
      return;
    }

    document.body.style.overflow = "";
  }, [isModal]);

  useEffect(() => {
    const paginationItems = document.querySelectorAll(".rc-pagination-item");
    paginationItems.forEach((item) => {
      if (disabledPages.includes(+item.textContent)) {
        item.classList.add("disabled");
      } else {
        item.classList.remove("disabled");
      }
    });
  }, [adv.number, disabledPages]);

  useEffect(() => {
    getAdv();
  }, [isSubmit]);

  return (
    <Styles.SearchingResults>
      <Modal isModal={isModal} setIsModal={setIsModal}>
        <Filtration submit={submit} isLoading={isLoading} />
      </Modal>

      <Responsive.Desktop>
        <Styles.BreadCrumbs>
          {getTranslatedText("filterPage.home")}
          <Styles.Span>
            {getTranslatedText("filterPage.searchResults")}
          </Styles.Span>

          <Title text={getTranslatedText("filterPage.searchResults")} />
        </Styles.BreadCrumbs>
      </Responsive.Desktop>

      <Styles.SearchingContent>
        <Responsive.Desktop>
          <Styles.FilterContainer>
            <Filtration submit={submit} isLoading={isLoading} />
          </Styles.FilterContainer>
        </Responsive.Desktop>

        {adv.content?.length > 0 && (
          <Styles.PaginationContainer onClick={handleClear}>
            <Responsive.NotDesktop>
              <Responsive.NotMobile>
                <Styles.BreadCrumbs>
                  {getTranslatedText("filterPage.home")}
                  <Styles.Span>
                    {getTranslatedText("filterPage.searchResults")}
                  </Styles.Span>

                  <Title text={getTranslatedText("filterPage.searchResults")} />
                </Styles.BreadCrumbs>
              </Responsive.NotMobile>

              <Responsive.Tablet>
                <Styles.TabletButtonContainer onClick={() => setIsModal(true)}>
                  <Icon.FilterBtn />
                </Styles.TabletButtonContainer>
              </Responsive.Tablet>

              <Responsive.Mobile>
                <Title
                  style={{ justifyContent: "start", marginBottom: "40px" }}
                  text={getTranslatedText("filterPage.searchResults")}
                />

                <Styles.MobileButtonContainer>
                  <ButtonNew
                    height="30px"
                    width="160px"
                    colorType={"blue"}
                    styleType={"outline"}
                    onClick={() => setIsModal(true)}
                    text={getTranslatedText("categoriesTitle.filter")}
                  />
                </Styles.MobileButtonContainer>
              </Responsive.Mobile>
            </Responsive.NotDesktop>

            <PagePagination
              onChange={getAdv}
              isLoading={isLoading}
              current={adv.number + 1}
              pageSize={adv?.size || 1}
              total={adv.totalElements}
              showMore={handleShowMore}
              text={getTranslatedText("paginationBtnText.showMore")}
            >
              {[
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

            <Styles.ToUp
              onClick={() => window.scrollTo({ top: 0, behavior: "smooth" })}
            >
              <Styles.Image />
            </Styles.ToUp>
          </Styles.PaginationContainer>
        )}
      </Styles.SearchingContent>
    </Styles.SearchingResults>
  );
};

export { SearchResults };
