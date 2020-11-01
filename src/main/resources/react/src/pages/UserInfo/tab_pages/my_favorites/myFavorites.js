import React from "react";

import "./myFavorites.scss";
import TitleBigBlue from "../../../../components/title_Big_Blue/title_Big_Blue";
import CardWithStar from "../../components/cards/card-with-star/card-with-star";

const MyFavorites = () => {
    const arr = [{
        elem: {
            title: "Название",
            city: "Киев",
            subject: "https://static.toiimg.com/photo/72975551.cms"
        }
    }];

    return (
        <section id="content3" className="tabs__content">
            <div className="content">
                <TitleBigBlue whatClass={"myProfile-title"} text={"Избранные объявления"}/>
                <div className="cards_replies">
                    {arr.map(elem => {
                        return (
                            <>
                                <CardWithStar title={elem.title} city={elem.city} subject={elem.subject}/>
                                <CardWithStar/>
                                <CardWithStar/>
                                <CardWithStar/>
                                <CardWithStar/>
                                <CardWithStar/>
                            </>
                        )
                    })}
                </div>
            </div>
        </section>
    )
};

export default MyFavorites;
