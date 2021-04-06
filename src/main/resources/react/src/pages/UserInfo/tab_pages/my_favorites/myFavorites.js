import React from "react";

import TitleBigBlue from "../../../../components/title_Big_Blue/title_Big_Blue";
import ProductCard from '../../../../components/Card/ProductCard';

import "./myFavorites.scss";

const MyFavorites = () => {

    const isFavorite = true;

    return (
        <section id="content3" className="tabs__content">
            <div className="content">
                <TitleBigBlue whatClass={"myProfile-title"} text={"Избранные объявления"}/>
                <div className="cards_replies">
                    <ProductCard
                      city={ 'Харьков' }
                      text={ `Велосипед ну очень куртой. просто не реально крутой для девочки 5 лет` }
                      picture={ "https://static.toiimg.com/photo/72975551.cms" }
                      isFavorite={ isFavorite }
                    />

                    <ProductCard
                      city={ 'Харьков' }
                      text={ `Велосипед ну очень куртой. просто не реально крутой для девочки 5 лет` }
                      picture={ "https://static.toiimg.com/photo/72975551.cms" }
                      isFavorite={ isFavorite }
                    />
                    <ProductCard
                      city={ 'Харьков' }
                      text={ `Велосипед ну очень куртой. просто не реально крутой для девочки 5 лет` }
                      picture={ "https://static.toiimg.com/photo/72975551.cms" }
                      isFavorite={ isFavorite }
                    />
                    <ProductCard
                      city={ 'Харьков' }
                      text={ `Велосипед ну очень куртой. просто не реально крутой для девочки 5 лет` }
                      picture={ "https://static.toiimg.com/photo/72975551.cms" }
                      isFavorite={ isFavorite }
                    />
                    <ProductCard
                      city={ 'Харьков' }
                      text={ `Велосипед ну очень куртой. просто не реально крутой для девочки 5 лет` }
                      picture={ "https://static.toiimg.com/photo/72975551.cms" }
                      isFavorite={ isFavorite }
                    />
                    <ProductCard
                      city={ 'Харьков' }
                      text={ `Велосипед ну очень куртой. просто не реально крутой для девочки 5 лет` }
                      picture={ "https://static.toiimg.com/photo/72975551.cms" }
                      isFavorite={ isFavorite }
                    />
                </div>
            </div>
        </section>
    )
};

export default React.memo(MyFavorites);
