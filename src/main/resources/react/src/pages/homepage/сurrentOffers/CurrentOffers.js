import React from "react";
import { db } from "./goods";

import ButtonBlue from "../../../components/buttonBlue/ButtonBlue.js";
import TitleBigBlue from "../../../components/title_Big_Blue";

import "./currentOffers.scss";
const CurrentOffers = () => {
  return (
    <section className="products-section">
      <div className="products-header">
        <TitleBigBlue
          whatClass="products-title-list"
          text="Текущие предложения"
        />
        <a href="#" className="products-link-search">
          Расширенный поиск
        </a>
      </div>

      <ul className="products-list" id="products-list">
        {db.map(data => (
          <li className="products-list-item" key={data.id}>
            <span
              className={`product-like-this ${
                data.isFavourite ? "activeFavourite" : ""
              }`}
            >
              <svg
                width="28"
                height="28"
                viewBox="0 0 28 28"
                fill="none"
                xmlns="http://www.w3.org/2000/svg"
              >
                <path
                  d="M14 22.0816L22.6525 27.2973L20.3631 17.4589L28 10.8432L17.931 9.97576L14 0.697327L10.069 9.97576L0 10.8432L7.63686 17.4589L5.34745 27.2973L14 22.0816Z"
                  fill="white"
                />
              </svg>
            </span>

            <div className="products-list-item__img">
              <img
                src={require(`../../../img/cards/${data.product.images[0].resourceUrl}`)}
                alt="lot"
                className="products-list-item__img-box"
              />
            </div>

            <div className="products-list-item__title">
              {data.description} {data.product.age} лет
            </div>
           
            <div className="products-list-item__footer">
            <span className="products-list-item__location">
              <span className="icon-location"> </span>
              {data.location.city}
            </span>
              <ButtonBlue
                text="Смотреть"
                href={"#"}
                whatClass="products-list-item__footer__bottom"
              />
            </div>
          </li>
        ))}
      </ul>
    </section>
  );
};

export default CurrentOffers;
