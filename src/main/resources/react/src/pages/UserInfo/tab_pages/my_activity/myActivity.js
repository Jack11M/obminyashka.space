import React from "react";

import "./myActyvity.scss";
import ButtonBlue from "../../../../components/buttonBlue/ButtonBlue";
import TitleBigBlue from "../../../../components/title_Big_Blue";

const MyActivity = () => {
  return (
    <section id="content1" className="tabs__content">
      <div className="content">
        <TitleBigBlue text={"входящие ответы"} />
        <ul className={'products-list'}>
        <li className="products-list-item">
          <span className={`product-like-this activeFavourite`}>
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
            <img src={""} alt="lot" className="products-list-item__img-box" />
          </div>

          <div className="products-list-item__title">5 лет</div>
          <span className="products-list-item__location">
            <span className="icon-location"/>
            Харьков
          </span>
          <div className="products-list-item__footer">
            <ButtonBlue
              text="Смотреть"
              href={"#"}
              whatClass="products-list-item__footer__bottom"
            />
          </div>
        </li>
        <li className="products-list-item">
          <span className={`product-like-this activeFavourite`}>
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
            <img src={""} alt="lot" className="products-list-item__img-box" />
          </div>

          <div className="products-list-item__title">5 лет</div>
          <span className="products-list-item__location">
            <span className="icon-location"/>
            Харьков
          </span>
          <div className="products-list-item__footer">
            <ButtonBlue
              text="Смотреть"
              href={"#"}
              whatClass="products-list-item__footer__bottom"
            />
          </div>
        </li>
        <li className="products-list-item">
          <span className={`product-like-this activeFavourite`}>
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
            <img src={""} alt="lot" className="products-list-item__img-box" />
          </div>

          <div className="products-list-item__title">5 лет</div>
          <span className="products-list-item__location">
            <span className="icon-location"/>
            Харьков
          </span>
          <div className="products-list-item__footer">
            <ButtonBlue
              text="Смотреть"
              href={"#"}
              whatClass="products-list-item__footer__bottom"
            />
          </div>
        </li>
        </ul>

        <TitleBigBlue text={"исходящие ответы"} />
        <li className="products-list-item">
          <span className={`product-like-this`}>
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
            <img src={""} alt="lot" className="products-list-item__img-box" />
          </div>

          <div className="products-list-item__title">5 лет</div>
          <span className="products-list-item__location">
            <span className="icon-location"/>
            Харьков
          </span>
          <div className="products-list-item__footer">
            <ButtonBlue
              text="Смотреть"
              href={"#"}
              whatClass="products-list-item__footer__bottom"
            />
          </div>
        </li>
        <li className="products-list-item">
          <span className={`product-like-this activeFavourite`}>
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
            <img src={""} alt="lot" className="products-list-item__img-box" />
          </div>

          <div className="products-list-item__title">5 лет</div>
          <span className="products-list-item__location">
            <span className="icon-location"/>
            Харьков
          </span>
          <div className="products-list-item__footer">
            <ButtonBlue
              text="Смотреть"
              href={"#"}
              whatClass="products-list-item__footer__bottom"
            />
          </div>
        </li>
      </div>
    </section>
  );
};

export default MyActivity;
