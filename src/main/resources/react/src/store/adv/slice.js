import { createSlice } from '@reduxjs/toolkit';

const initialState = {
  topic: '',
  category: '',
  subcategory: '',
  readyForOffers: [],
  wishesToExchange: [],
  ageStore: [],
  genderStore: [],
  seasonStore: [],
  sizeStore: [],
  descriptionStore: '',
  idLocation: '',
  fileImage: [],
  currLocation: null,
  locationShow: { city: '', area: '' },
  viewImage: [],
  indexImage: null,
};

const advSlice = createSlice({
  name: 'advertisement',
  initialState,
  reducers: {
    saveAdv: (state, { payload }) => {
      state.topic = payload.announcementTitle;
      state.category = payload.categoryItems;
      state.subcategory = payload.subCategoryItems;
      state.readyForOffers = payload.readyOffer;
      state.wishesToExchange = payload.exchangeList;
      state.ageStore = payload.age;
      state.genderStore = payload.gender;
      state.seasonStore = payload.season;
      state.sizeStore = payload.size;
      state.descriptionStore = payload.description;
      state.idLocation = payload.locationId;
      state.fileImage = payload.imageFiles;
      state.currLocation = payload.locationCurrent;
      state.locationShow = payload.showLocation;
      state.viewImage = payload.preViewImage;
      state.indexImage = payload.currentIndexImage;
    },
    clearAdv: (state) => {
      state.topic = '';
      state.category = '';
      state.subcategory = '';
      state.readyForOffers = [];
      state.wishesToExchange = [];
      state.ageStore = [];
      state.genderStore = [];
      state.seasonStore = [];
      state.sizeStore = [];
      state.descriptionStore = '';
      state.idLocation = '';
      state.fileImage = [];
      state.currLocation = null;
      state.locationShow = { city: '', area: '' };
      state.viewImage = [];
      state.indexImage = null;
    },
  },
});

const {
  reducer: advReducer,
  actions: { saveAdv, clearAdv },
} = advSlice;

const getAdv = (state) => state.adv;

export { saveAdv, clearAdv, advReducer, getAdv };
