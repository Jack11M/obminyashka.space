import React from "react";
import './card-with-star.scss';

const CardWithStar = ({title, city, subject}) => {
    return (
        <div className="card-2">
            <div className="card-2_header">
                <img src={require('../../../../../img/star.png')} alt="img"/>
            </div>
            <figure className="card-2_figure">
                <img
                    src={subject ? subject : require('../../../../../img/MySettings/mustang-princessa-mustang-princess-12141820-velosiped-detskii-photo-064d 1.png')}
                    alt="img"/>
            </figure>
            <h6 className="card-2_title">{title ? title : "Велосипед детский для девочки 5 лет"}</h6>
            <img src={require('../../../../../img/MySettings/location.svg')} className="card_location" alt="img"/>
            <span className="card-2_location-city">{city ? city : "Харьков"}</span>
            <div className="card-2_footer">
                <button className="card-2_button">СМОТРЕТЬ</button>
            </div>
        </div>
    )
}

export default CardWithStar;
