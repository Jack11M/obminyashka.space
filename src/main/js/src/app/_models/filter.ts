import { ICategory } from './category';
import { ISubcategory } from './subcategory';

export interface IFilter {
    category_id: number;
    name: string;
    filterGender: string[];
    filterSeason: string[];
    filterLocation: string;
    filterAge: string[];
    filterSize: string[];
    sortByCreated: boolean;
    pagging: {page: number, elementsOnPage: number};
}
