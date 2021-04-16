import React from "react";
import { Link } from 'react-router-dom';
import { useSelector } from 'react-redux';

import lot3 from '../../../img/cards/lot3.jpg';
import TitleBigBlue from "../../../components/title_Big_Blue";
import ProductCard from '../../../components/Card';

import "./currentOffers.scss";
import { getTranslatedText } from '../../../components/local/localisation';

const CurrentOffers = () => {
  const { lang } = useSelector( state => state.auth );

  return (
    <section className="products-section">
      <div className="products-header">
        <TitleBigBlue
          whatClass="products-title-list"
          text={getTranslatedText('mainPage.blueText', lang)}
        />
        <Link to="/" className="products-link-search">
          {getTranslatedText('mainPage.blueSearch', lang)}
        </Link>
      </div>

      <ul className="products-list" id="products-list">
        <li>
          <ProductCard
            margin={'10px 8px'}
            isFavorite={ true }
            picture={ lot3 }
            city={ 'Харьков' }
            text={ `Велосипед ну очень крутой, просто не реально крутой для девочки 5 лет` }/>
        </li>
        <li>
          <ProductCard
            margin={'10px 8px'}
            isFavorite={ false }
            picture={ lot3 }
            city={ 'Харьков' }
            text={ `Велосипед ну очень крутой, просто не реально крутой для девочки 5 лет` }/>
        </li>
        <li>
          <ProductCard
            margin={'10px 8px'}
            isFavorite={ true }
            picture={ lot3 }
            city={ 'Харьков' }
            text={ `Велосипед ну очень крутой, просто не реально крутой для девочки 5 лет` }/>
        </li>
        <li>
          <ProductCard
            margin={'10px 8px'}
            isFavorite={ false }
            picture={ lot3 }
            city={ 'Харьков' }
            text={ `Велосипед ну очень крутой, просто не реально крутой для девочки 5 лет` }/>
        </li>
        <li>
          <ProductCard
            margin={'10px 8px'}
            isFavorite={ false }
            picture={ lot3 }
            city={ 'Харьков' }
            text={ `Велосипед ну очень крутой, просто не реально крутой для девочки 5 лет` }/>
        </li>
        <li>
          <ProductCard
            margin={'10px 8px'}
            isFavorite={ true }
            picture={ lot3 }
            city={ 'Харьков' }
            text={ `Велосипед ну очень крутой, просто не реально крутой для девочки 5 лет` }/>
        </li>
        <li>
          <ProductCard
            margin={'10px 8px'}
            isFavorite={ true }
            picture={ lot3 }
            city={ 'Харьков' }
            text={ `Велосипед ну очень крутой, просто не реально крутой для девочки 5 лет` }/>
        </li>
        <li>
          <ProductCard
            margin={'10px 8px'}
            isFavorite={ true }
            picture={ lot3 }
            city={ 'Харьков' }
            text={ `Велосипед ну очень крутой, просто не реально крутой для девочки 5 лет` }/>
        </li>
      </ul>
    </section>
  );
};

export default CurrentOffers;
