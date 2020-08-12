import React, { Component } from "react";
import { Link } from "react-router-dom";
import Slider from "react-slick";
import "./slider.scss";
import "slick-carousel/slick/slick.scss";
import "slick-carousel/slick/slick-theme.scss";

import {
  childSlider,
  clothesSlider,
  furnitureSlider,
  shoesSlider,
  strollersSlider,
  toySlider,
} from "../../../img/all_images_export/sliderImages";

class Sliders extends Component {
  isImg = [
    {
      src: toySlider,
      subtitle: "Огромный выбор",
      title: "Игрушек",
      href: "#",
      width: 290,
    },
    {
      src: clothesSlider,
      subtitle: "Разнообразие детской и подростковой",
      title: "Одежды",
      href: "#",
      width: 600,
    },
    {
      src: childSlider,
      subtitle: "Всё для",
      title: "Малышей",
      href: "#",
      width: 290,
    },
    {
      src: furnitureSlider,
      subtitle: "Множество детской",
      title: "Мебели",
      href: "#",
      width: 290,
    },
    {
      src: shoesSlider,
      subtitle: "Разнообразие детской и подростковой",
      title: "Обуви",
      href: "#",
      width: 600,
    },

    {
      src: strollersSlider,
      subtitle: "Детский",
      title: "Транспорт",
      href: "#",
      width: 290,
    },
  ];

  render() {
    const settings = {
      className: "slider variable-width",
      dots: false,
      arrows: false,
      infinite: true,
      centerMode: false,
      slidesToShow: 3,
      slidesToScroll: 1,
      autoplay: true,
      speed: 2000,
      autoplaySpeed: 4000,
      cssEase: "ease-in-out",
      variableWidth: true,
    };

    return (
      <div className="Home-page-slider">
        <Slider {...settings}>
          {this.isImg.map((image, idx) => (
            <Link
              className={"Home-page-slider__link"}
              to={image.href}
              style={{ width: image.width }}
              key={idx + image.title}
            >
              <img src={image.src} alt={image.title} />
              <span className={"Home-page-slider__link-subTitle"}>
                {image.subtitle}
                <br />
                <b>{image.title}</b>
              </span>
            </Link>
          ))}
        </Slider>
      </div>
    );
  }
}

export default Sliders;
