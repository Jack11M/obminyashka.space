import React, { useState, useEffect } from "react";
import { Link } from "react-router-dom";

import "./slider.scss";
import toySlider from "../../../../img/slider/toy.jpg";
import clothesSlider from "../../../../img/slider/clothes.jpg";
import childSlider from "../../../../img/slider/child.jpg";
import furnitureSlider from "../../../../img/slider/furniture.jpg";
import shoesSlider from "../../../../img/slider/shoes.jpg";
import strollersSlider from "../../../../img/slider/strollers.jpg";
import booksSlider from "../../../../img/slider/books.jpg";

const Sliders = () => {
  const DELAY = 3000;
  const PLAY_SPEED = 2000;
  const [x, setX] = useState(0);
  const [go, setGo] = useState(false);
  const [play, setPlay] = useState(false);
  const [delay, setDelay] = useState(0);
  const [isImg, setIsImg] = useState([
    {
      src: toySlider,
      subtitle: "Огромный выбор",
      title: "Игрушек",
      href: "#",
      width: 290
    },
    {
      src: clothesSlider,
      subtitle: "Разнообразие детской и подростковой",
      title: "Одежды",
      href: "#",
      width: 600
    },
    {
      src: childSlider,
      subtitle: "Всё для",
      title: "Малышей",
      href: "#",
      width: 290
    },
    {
      src: furnitureSlider,
      subtitle: "Множество детской",
      title: "Мебели",
      href: "#",
      width: 290
    },
    {
      src: shoesSlider,
      subtitle: "Разнообразие детской и подростковой",
      title: "Обуви",
      href: "#",
      width: 600
    },

    {
      src: strollersSlider,
      subtitle: "Детский",
      title: "Транспорт",
      href: "#",
      width: 290
    },

    {
      src: booksSlider,
      subtitle: "Развивающие и детские",
      title: "Книги",
      href: "#",
      width: 290
    },
    {
      src: shoesSlider,
      subtitle: "Разнообразие детской и подростковой",
      title: "Обуви",
      href: "#",
      width: 600
    },
    {
      src: booksSlider,
      subtitle: "Развивающие и детские",
      title: "Книги",
      href: "#",
      width: 290
    }
  ]);

  useEffect(() => {
    if (x < 0) {
      return;
    }
    const autoPlaySpeed = setTimeout(() => {
      setX(x - (isImg[0].width + 22));
      setDelay(PLAY_SPEED);
      setPlay(true);
    }, DELAY);
    return () => clearTimeout(autoPlaySpeed);
  }, [go]);

  useEffect(() => {
    if (!play) {
      return;
    }
    const playSpeed = setTimeout(() => {
      setGo(true);
      setPlay(false);
    }, PLAY_SPEED);
    return () => clearTimeout(playSpeed);
  }, [play]);

  useEffect(() => {
    if (!go) {
      return;
    }
    setDelay(0);
    setX(0);
    const arr = [...isImg];
    const el = arr.shift();
    arr.push(el);
    setIsImg(() => arr);
    setGo(false);
  }, [go]);

  return (
    <div className="Home-page-slider">
      {/*<div className="wrapper">*/}
      <div className="Home-page-slider__element">
        {isImg.map((image, idx) => (
          <Link
            to={image.href}
            className={"Home-page-slider__element__box"}
            style={{
              transform: `translateX(${x}px)`,
              transition: `${delay}ms ease`
            }}
            key={idx + image.title}
          >
            <img
              style={{ width: image.width }}
              src={image.src}
              alt={image.title}
            />
            <span className={"Home-page-slider__element__box-subTitle"}>
              {image.subtitle}
              <br />
              <b>{image.title}</b>
            </span>
          </Link>
        ))}
      </div>
    </div>
    // </div>
  );
};

export default Sliders;
