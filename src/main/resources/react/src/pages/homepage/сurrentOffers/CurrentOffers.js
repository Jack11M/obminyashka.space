import React, { useState, useEffect } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useSelector } from 'react-redux';

import api from 'REST/Resources';
import { route } from 'routes/routeConstants';
import ProductCard from 'components/item-card';
import noPhotos from 'assets/img/showAdv/noPhoto.svg';
import TitleBigBlue from 'components/common/title_Big_Blue';
import { getTranslatedText } from 'components/local/localisation';

import './currentOffers.scss';

const CurrentOffers = () => {
  const navigate = useNavigate();
  const { lang } = useSelector((state) => state.auth);

  const [offers, setOffers] = useState([]);

  useEffect(() => {
    api.fetchHome
      .getCurrentOffers()
      .then(({ data }) => {
        if (Array.isArray(data)) setOffers(data);
      })
      .catch((e) => {
        console.log(e.response);
      });
  }, []);

  const moveToProductPage = (id) => {
    navigate(route.productPage.replace(':id', id));
  };

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
        {offers.map((offer) => (
          <li key={offer.advertisementId}>
            <ProductCard
              clickOnButton={() => moveToProductPage(offer.advertisementId)}
              margin={'10px 8px'}
              isFavorite={false}
              picture={
                offer.image ? `data:image/jpeg;base64,${offer.image}` : noPhotos
              }
              city={offer.location.city || ''}
              text={offer.title}
            />
          </li>
        ))}
      </ul>
    </section>
  );
};

export default CurrentOffers;
