import { regions } from './mock';

export const initialValues = {
  age: [],
  sex: '',
  season: [],
  'size(shoes)': [],
  'size(clothes)': [],
  category: {
    id: '',
    subcategories: [],
    activeCategory: '',
  },
  region: {
    id: '',
    location: regions,
    inputValue: '',
  },
  city: {
    id: '',
    location: [],
    inputValue: '',
  },
};
