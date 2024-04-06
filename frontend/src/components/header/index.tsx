import { useContext, useEffect } from "react";
import { useWindowSize } from "react-use";
import {
  Link,
  useLocation,
  useNavigate,
  useSearchParams,
} from "react-router-dom";
import {
  Logo,
  Deals,
  Avatar,
  Burger,
  Search,
  ButtonNew,
  Responsive,
  CategoryButton,
  InputChangeEventType,
} from "obminyashka-components";

import { route } from "src/routes/routeConstants";
import { getProfile } from "src/store/profile/slice";
import { SearchContext } from "src/components/common";
import { getUserThunk } from "src/store/profile/thunk";
import { useAppDispatch, useAppSelector } from "src/store";
import { getAuth, getAuthed, setLanguage } from "src/store/auth/slice";

import * as Styles from "./styles";
import { getTranslatedText } from "../local";
import { SelectLanguage } from "../selectLang";
import { burgerLinks, categories } from "./config";

const Header = () => {
  const navigate = useNavigate();
  const dispatch = useAppDispatch();
  const { width } = useWindowSize();
  const { pathname } = useLocation();

  const { lang } = useAppSelector(getAuth);
  const isAuthed = useAppSelector(getAuthed);
  const profile = useAppSelector(getProfile);

  const [searchParams, setSearchParams] = useSearchParams();

  const { search, setSearch, setIsFetch } = useContext(SearchContext);

  const handleSelected = (langValue: { lang: string }) => {
    dispatch(setLanguage(langValue.lang));
    navigate(0);
  };

  const move = () => {
    setIsFetch(true);
    const currentParams = Object.fromEntries(searchParams);

    if (!search) return;

    currentParams.search = search;
    setSearchParams(currentParams);

    if (pathname?.replace("/", "") !== route.SearchResults) {
      navigate(route.SearchResults);
    }
  };

  const keyEnter = (event: React.KeyboardEvent<HTMLInputElement>) => {
    if (!search) {
      if (event.key === "Enter") event.preventDefault();
      return;
    }

    if (event.key === "Enter") {
      event.preventDefault();
      move();
    }
  };

  useEffect(() => {
    if (!profile && isAuthed) {
      dispatch(getUserThunk());
    }
  }, [dispatch, isAuthed, profile]);

  const onChange = (e: InputChangeEventType | string) => {
    setIsFetch(false);
    setSearch(typeof e === "string" ? e : e.target.value);
  };

  return (
    <Styles.Header>
      <Styles.HeaderOverlay>
        <Styles.Container>
          <Logo to={route.home} />

          <CategoryButton
            categoryInfo={categories}
            textBtn={getTranslatedText("header.categories")}
            isDisabled={pathname?.replace("/", "") === route.SearchResults}
          />

          <Responsive.Desktop>
            <Deals
              to={route.home}
              heartIcon={true}
              text={getTranslatedText("header.goodness")}
            />

            <Search
              onClick={move}
              value={search}
              onChange={onChange}
              onKeyDown={keyEnter}
            />

            <Deals
              to={route.home}
              puzzleIcon={true}
              text={getTranslatedText("header.about")}
            />
          </Responsive.Desktop>

          <Responsive.NotMobile>
            <Styles.BtnContainer width={width}>
              <ButtonNew
                plus
                animated
                colorType="green"
                styleType="default"
                onClick={() => navigate(route.addAdv)}
                text={getTranslatedText("button.addAdv")}
              />
            </Styles.BtnContainer>
          </Responsive.NotMobile>

          <Responsive.NotDesktop>
            <Search
              value={search}
              onClick={move}
              onChange={onChange}
              onKeyDown={keyEnter}
            />
          </Responsive.NotDesktop>

          <Responsive.Desktop>
            <Styles.LngAvatarContainer width={width}>
              <SelectLanguage />

              <Link to={isAuthed ? route.userInfo : route.login}>
                <Avatar width={50} height={50} source={profile?.avatarImage} />
              </Link>
            </Styles.LngAvatarContainer>
          </Responsive.Desktop>

          <Burger
            lang={lang}
            data={burgerLinks}
            onSelectLanguage={handleSelected}
          />
        </Styles.Container>
      </Styles.HeaderOverlay>
    </Styles.Header>
  );
};

export { Header };
