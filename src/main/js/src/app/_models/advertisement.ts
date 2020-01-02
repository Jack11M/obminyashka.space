import { ICategory } from './category';
import { ISubcategory } from './subcategory';

export interface IAdvertisement {
    id: number;
    dealType: string;
    description: string;
    isFavourite: boolean;//isFavourite: boolean;
    location: string;
    city: string;
    district: string;
    readyForOffers: boolean;
    topic: string;
    wishesToExchange: string;
    created: Date;
    updated: Date;

    img: string;
    title: string;


    //"product": {
    age: string;
    gender: string;
    product_id: number;
    // images: [{}];//defaultPhoto, id, resourceUrl
    defaultPhoto: boolean;
    season: string;
    size: string;

    category: number;
    subcategory: number;
    category_: ICategory;
    subcategory_: ISubcategory;
}