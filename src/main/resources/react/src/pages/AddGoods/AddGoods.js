import React, { useState } from 'react';
import * as Yup from 'yup';
import { Form } from 'formik';
import { useHistory } from 'react-router-dom';
import { useDispatch, useSelector } from 'react-redux';

import api from 'REST/Resources';
import ru from 'components/local/ru';
import { enumAge } from 'config/ENUM.js';
import { route } from 'routes/routeConstants';
import { saveAdv, clearAdv } from 'store/adv/slice';
import Button from 'components/common/buttons/button/Button';
import { getTranslatedText } from 'components/local/localisation';
import ButtonAdv from 'components/common/buttons/buttonAdv/ButtonAdv';
import { FormHandler, FormikCheckBox } from 'components/common/formik';

import { Sizes } from './sizes';
import { Location } from './location';
import { Exchange } from './exchange';
import { PhotoFiles } from './photo-files';
import { SelectionSection } from './selection-section';
import { WrapCharacteristic } from './wrap-characteristic';

import './AddGoods.scss';

const AddGoods = () => {
  const history = useHistory();
  const dispatch = useDispatch();
  const { genderEnum, seasonEnum } = ru;
  const { lang } = useSelector((state) => state.auth);
  const adv = useSelector((state) => state.adv);

  const regexp = /data:image\/(jpg|jpeg|png|gif);base64,/;

  const [buttonPreview, setButtonPreview] = useState(false);

  const [announcementTitle, setAnnouncementTitle] = useState(adv.topic);
  const [exchangeList, setExchangeList] = useState(adv.wishesToExchange);
  const [description, setDescription] = useState(adv.descriptionStore);
  const [locationId, setLocationId] = useState(adv.idLocation);
  const [imageFiles, setImageFiles] = useState(adv.fileImage);

  const [categoryItems, setCategoryItems] = useState(adv.category);
  const [subCategoryItems, setSubCategoryItems] = useState(adv.subcategory);
  const [readyOffer, setReadyOffer] = useState(adv.readyForOffers);
  const [age, setAge] = useState(adv.ageStore);
  const [gender, setGender] = useState(adv.genderStore);
  const [season, setSeason] = useState(adv.seasonStore);
  const [size, setSize] = useState(adv.sizeStore);
  const [locationCurrent, setLocationCurrent] = useState(adv.currLocation);
  const [showLocation, setShowLocation] = useState(adv.locationShow);

  const [preViewImage, setPreViewImage] = useState(adv.viewImage);
  const [currentIndexImage, setCurrentIndexImage] = useState(adv.indexImage);

  const validationAdv = Yup.object().shape({
    id: Yup.number().default(() => 0),
    dealType: Yup.string().default(() => 'EXCHANGE'),
    categoryId: Yup.number()
      .required(getTranslatedText('errors.requireField', lang))
      .default(() => categoryItems?.id),
    subcategoryId: Yup.number()
      .required(getTranslatedText('errors.requireField', lang))
      .default(() => subCategoryItems?.id),
    topic: Yup.string()
      .required(getTranslatedText('errors.requireField', lang))
      .min(3, getTranslatedText('errors.min3', lang))
      .max(30, getTranslatedText('errors.max5', lang))
      .default(() => announcementTitle),
    readyForOffers: Yup.boolean().default(() => !!readyOffer.length),
    wishesToExchange: Yup.string()
      .required(getTranslatedText('errors.requireField', lang))
      .default(() => exchangeList.join(',')),
    age: Yup.string()
      .required(getTranslatedText('errors.requireField', lang))
      .default(() => age.join('')),
    gender: Yup.string()
      .required(getTranslatedText('errors.requireField', lang))
      .default(() => gender.join('')),
    season: Yup.string()
      .required()
      .default(() => season.join('')),
    size: Yup.string()
      .when('categoryId', {
        is: (value) => value === 1,
        then: Yup.string().required(
          getTranslatedText('errors.requireField', lang)
        ),
      })
      .when('categoryId', {
        is: (value) => value === 2,
        then: Yup.string().required(
          getTranslatedText('errors.requireField', lang)
        ),
      })
      .default(() => size),
    description: Yup.string()
      .max(255, getTranslatedText('errors.max255', lang))
      .default(() => description),
    locationId: Yup.number()
      .nullable()
      .required(getTranslatedText('errors.requireField', lang))
      .default(() => locationId),
    images: Yup.array()
      .test(
        '',
        getTranslatedText('errors.minPhoto', lang),
        (photo) => !!photo.length
      )
      .default(() => imageFiles),
  });

  const createAdvertisement = async (values) => {
    setButtonPreview(false);
    const { images, size, ...rest } = values;
    let data = new FormData();
    if (size) {
      data.append('dto', JSON.stringify({ ...rest, size }));
    } else data.append('dto', JSON.stringify(rest));

    images.forEach((item) => data.append('image', item));

    try {
      await api.fetchAddGood.sendNewAdv(data);
      dispatch(clearAdv());
      history.push(route.home);
    } catch (err) {
      console.log(err.response.data);
    }
  };

  const previewSentToPage = () => {
    setButtonPreview(false);
    dispatch(
      saveAdv({
        age,
        size,
        gender,
        season,
        locationId,
        imageFiles,
        readyOffer,
        description,
        exchangeList,
        showLocation,
        preViewImage,
        categoryItems,
        locationCurrent,
        subCategoryItems,
        announcementTitle,
        currentIndexImage,
      })
    );

    history.push(route.productPage, {
      wishes: exchangeList,
      category: categoryItems,
      subcategory: subCategoryItems,
      currentLocation: locationCurrent,
      product: {
        age,
        size,
        gender,
        season,
        description,
        topic: announcementTitle,
        readyForOffers: !!readyOffer.length,
      },
      photos: preViewImage.map((photo, index) => ({
        id: index,
        resource: photo.replace(regexp, ''),
      })),
    });
  };

  const handleSubmit = async (values) => {
    if (buttonPreview) previewSentToPage();
    else createAdvertisement(values);
  };

  const initialValues = validationAdv.cast({});

  const agesShow = Object.keys(enumAge);
  const sexShow = Object.keys(genderEnum);
  const seasonShow = Object.keys(seasonEnum);

  const resetAll = () => {
    setAnnouncementTitle('');
    setExchangeList([]);
    setDescription('');
    setLocationId(null);
    setImageFiles([]);
    setCategoryItems('');
    setSubCategoryItems('');
    setReadyOffer([]);
    setAge([]);
    setGender([]);
    setSeason([]);
    setSize('');
    setLocationCurrent(null);
    setPreViewImage([]);
    setCurrentIndexImage(null);
    setShowLocation({ city: '', area: '' });
    dispatch(clearAdv());
  };

  return (
    <FormHandler
      onSubmit={handleSubmit}
      initialValues={initialValues}
      validationSchema={validationAdv}
    >
      <Form>
        <main className="add">
          <div className="add_container">
            <div className="add_inner">
              <SelectionSection
                readyOffers={{ readyOffer, setReadyOffer }}
                category={{ categoryItems, setCategoryItems }}
                subcategory={{ subCategoryItems, setSubCategoryItems }}
                announcement={{ announcementTitle, setAnnouncementTitle }}
              />

              <Exchange
                exchangeList={exchangeList}
                setExchange={setExchangeList}
              />

              <div className="characteristics">
                <h3>{getTranslatedText(`addAdv.options`, lang)}</h3>

                <div className="characteristics_items">
                  <div className="characteristics_item">
                    <WrapCharacteristic
                      name="age"
                      title={getTranslatedText(`addAdv.age`, lang)}
                    >
                      {agesShow.map((item, idx) => (
                        <FormikCheckBox
                          name="age"
                          value={item}
                          type="radio"
                          key={item + idx}
                          onChange={setAge}
                          text={enumAge[item]}
                          selectedValues={age}
                          margin="0 0 15px -7px"
                        />
                      ))}
                    </WrapCharacteristic>
                  </div>

                  <div className="characteristics_item">
                    <WrapCharacteristic
                      name="gender"
                      title={getTranslatedText(`addAdv.sex`, lang)}
                    >
                      {sexShow.map((item, idx) => (
                        <FormikCheckBox
                          value={item}
                          type="radio"
                          name="gender"
                          key={item + idx}
                          onChange={setGender}
                          margin="0 0 15px -7px"
                          selectedValues={gender}
                          text={getTranslatedText(`genderEnum.${item}`, lang)}
                        />
                      ))}
                    </WrapCharacteristic>
                  </div>

                  <div className="characteristics_item">
                    <WrapCharacteristic
                      name="season"
                      title={getTranslatedText(`addAdv.season`, lang)}
                    >
                      {seasonShow.map((item, idx) => (
                        <FormikCheckBox
                          key={item + idx}
                          value={item}
                          name="season"
                          type="radio"
                          margin="0 0 15px -7px"
                          onChange={setSeason}
                          selectedValues={season}
                          text={getTranslatedText(`seasonEnum.${item}`, lang)}
                        />
                      ))}
                    </WrapCharacteristic>
                  </div>

                  <Sizes
                    categories={categoryItems}
                    dimension={{ size, setSize }}
                  />
                </div>
              </div>

              <div className="description">
                <h3 className="description_title">
                  {getTranslatedText(`addAdv.describeTitle`, lang)}
                </h3>

                <p className="description_subtitle">
                  <span className="span_star">*</span>
                  {getTranslatedText(`addAdv.describeText`, lang)}
                </p>

                <textarea
                  value={description}
                  className="description_textarea"
                  onChange={(e) => setDescription(e.target.value)}
                />
              </div>

              <Location
                setLocationId={setLocationId}
                setLocationCurrent={setLocationCurrent}
                onInputLocation={{ showLocation, setShowLocation }}
              />

              <PhotoFiles
                imageFiles={imageFiles}
                preViewImage={preViewImage}
                setImageFiles={setImageFiles}
                setPreViewImage={setPreViewImage}
                currentIndexImage={currentIndexImage}
                setCurrentIndexImage={setCurrentIndexImage}
              />

              <div className="bottom_block">
                <div className="buttons_block">
                  <ButtonAdv type="submit" />

                  <Button
                    type="submit"
                    whatClass="preview"
                    width={lang === 'ua' ? '270px' : '222px'}
                    click={() => setButtonPreview(true)}
                    text={getTranslatedText(`addAdv.preview`, lang)}
                  />
                </div>

                <div className="cancel" onClick={resetAll}>
                  <div className="cross" />
                  <p>{getTranslatedText(`addAdv.cancel`, lang)}</p>
                </div>
              </div>
            </div>
          </div>
        </main>
      </Form>
    </FormHandler>
  );
};

export default AddGoods;
