import React, { useState, useEffect } from 'react';
import { Link, useHistory } from 'react-router-dom';
import { useSelector } from 'react-redux';

import { route } from '../../../routes/routeConstants';
import ProductCard from '../../../components/Card';
import TitleBigBlue from '../../../components/title_Big_Blue';
import { getCurrentOffers } from '../../../REST/Resources';
import { getTranslatedText } from '../../../components/local/localisation';
import noPhotos from '../../../img/showAdv/noPhoto.svg';


import './currentOffers.scss';

const CurrentOffers = () => {
  const history = useHistory();
  const { lang } = useSelector(state => state.auth);
  //useState
  const [ offers, setOffers ] = useState([]);
  //useEffect
  useEffect(() => {
    getCurrentOffers()
      .then((data) => {
        setOffers(data);
      })
      .catch(e => {
        console.log(e);
      });
  });

  const moveToProductPage = (id) => {
    history.push(route.productPage + id)
  }

  return (
    <section className = "products-section">
      <div className = "products-header">
        <TitleBigBlue
          whatClass = "products-title-list"
          text = { getTranslatedText('mainPage.blueText', lang) }
        />
        <Link to = "/" className = "products-link-search">
          { getTranslatedText('mainPage.blueSearch', lang) }
        </Link>
      </div>

      <ul className = "products-list" id = "products-list">
        { offers.map((offer) =>
          <li>
            <ProductCard
              clicOnButton={() => moveToProductPage(1)} //offer.advertisementId
              margin = { '10px 8px' }
              isFavorite = { false }
              picture = { offer.image ? `data:image/jpeg;base64,${ offer.image  }` : noPhotos }
              city = { offer.location.city || '' }
              text = { offer.title }
            />
          </li>
        ) }
      </ul>
    </section>
  );
};

export default CurrentOffers;
