/* eslint-disable */
// @ts-nocheck
// TODO: fix typescript
import { useState } from 'react';
import { Form } from 'formik';
import { useNavigate } from 'react-router-dom';
import { useDispatch, useSelector } from 'react-redux';
import imageCompression from 'browser-image-compression';
import {
  Icon,
  Input,
  Button,
  Subtitle,
  BackButton,
  PhotoFiles,
  showMessage,
  CancelEverything,
} from 'obminyashka-components';

import { options } from 'src/Utils';
import api from 'src/REST/Resources';
import { route } from 'src/routes/routeConstants';
import { getErrorMessage } from 'src/Utils/error';
import { getAuthLang } from 'src/store/auth/slice';
import { FormikFocus } from 'src/components/common/formik';
import { enumAge, seasonEnum, enumSex } from 'src/config/ENUM';
import { clearAdv, getAdv, saveAdv } from 'src/store/adv/slice';
import { getTranslatedText } from 'src/components/local/localization';
import { FormikCheckBox, FormikHandler } from 'src/components/common';

import { Sizes } from './sizes';
import { Exchange } from './exchange';
import { Location } from './location';
import { SelectionSection } from './selection-section';
import { WrapCharacteristic } from './wrap-characteristic';

import * as Styles from './styles';
import { getValidationAdv } from './config';

const AddGoods = () => {
  const navigate = useNavigate();
  const dispatch = useDispatch();
  const adv = useSelector(getAdv);
  const lang = useSelector(getAuthLang);

  const [buttonPreview, setButtonPreview] = useState(false);
  const [isLoading, setIsLoading] = useState(false);

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

  const validationAdv = getValidationAdv({
    age,
    size,
    gender,
    season,
    readyOffer,
    locationId,
    imageFiles,
    description,
    exchangeList,
    categoryItems,
    subCategoryItems,
    announcementTitle,
  });

  const createAdvertisement = async (values) => {
    setButtonPreview(false);
    setIsLoading(true);
    const { images, size: sizeValue, ...rest } = values;
    const data = new FormData();
    if (sizeValue) {
      data.append('dto', JSON.stringify({ ...rest, sizeValue }));
    } else data.append('dto', JSON.stringify(rest));

    const compressedFiles = await Promise.all(
      images.map(async (item) => {
        const file = await imageCompression(item, options);
        return file;
      })
    );

    compressedFiles.forEach((item) => data.append('image', item));

    try {
      await api.addGood.sendNewAdv(data);
      dispatch(clearAdv());
      navigate(route.home);
    } catch (err) {
      setIsLoading(false);
      showMessage.error(getErrorMessage(err));
    }
  };

  const previewSentToPage = (values) => {
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
        categoryItems,
        locationCurrent,
        subCategoryItems,
        announcementTitle,
      })
    );

    navigate(route.prevProductPage, {
      state: {
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
        photos: values.images.map((photo, index) => ({
          id: index,
          resource: URL.createObjectURL(photo),
        })),
      },
    });
  };

  const handleSubmit = async (values) => {
    if (buttonPreview) previewSentToPage(values);
    else createAdvertisement(values);
  };

  const initialValues = validationAdv.cast({});

  const sexShow = Object.keys(enumSex);
  const agesShow = Object.keys(enumAge);
  const seasonShow = Object.keys(seasonEnum);

  const resetAll = () => {
    setAnnouncementTitle('');
    setExchangeList([]);
    setDescription('');
    setLocationId('');
    setImageFiles([]);
    setCategoryItems('');
    setSubCategoryItems('');
    setReadyOffer([]);
    setAge([]);
    setGender([]);
    setSeason([]);
    setSize('');
    setLocationCurrent(null);
    setShowLocation({ city: '', area: '' });
    dispatch(clearAdv());
  };

  return (
    <FormikHandler
      onSubmit={handleSubmit}
      initialValues={initialValues}
      validationSchema={validationAdv}
    >
      {() => (
        <Form>
          <Styles.MainContainer>
            <Styles.Container>
              <Styles.AddContainer>
                <BackButton
                  type='button'
                  style={{ marginBottom: 16 }}
                  text={getTranslatedText('button.back')}
                />
                <SelectionSection
                  category={{ categoryItems, setCategoryItems }}
                  subcategory={{ subCategoryItems, setSubCategoryItems }}
                  announcement={{ announcementTitle, setAnnouncementTitle }}
                />
                <Exchange
                  exchangeList={exchangeList}
                  setExchange={setExchangeList}
                  readyOffers={{ readyOffer, setReadyOffer }}
                />
                <>
                  <Subtitle textTitle={getTranslatedText('addAdv.options')} />

                  <Styles.WrapItems>
                    <Styles.SectionsItem>
                      <WrapCharacteristic name='age' title={getTranslatedText('addAdv.age')}>
                        {agesShow.map((item, idx) => (
                          <FormikCheckBox
                            name='age'
                            value={item}
                            type='radio'
                            onChange={setAge}
                            text={enumAge[item]}
                            selectedValues={age}
                            margin='0 0 15px 0'
                            key={String(item + idx)}
                          />
                        ))}
                      </WrapCharacteristic>
                    </Styles.SectionsItem>

                    <Styles.SectionsItem>
                      <WrapCharacteristic name='gender' title={getTranslatedText('addAdv.sex')}>
                        {sexShow.map((item, idx) => (
                          <FormikCheckBox
                            value={item}
                            type='radio'
                            name='gender'
                            margin='0 0 15px 0'
                            onChange={setGender}
                            selectedValues={gender}
                            key={String(item + idx)}
                            text={getTranslatedText(`genderEnum.${item}`)}
                          />
                        ))}
                      </WrapCharacteristic>
                    </Styles.SectionsItem>

                    <Styles.SectionsItem>
                      <WrapCharacteristic name='season' title={getTranslatedText('addAdv.season')}>
                        {seasonShow.map((item, idx) => (
                          <FormikCheckBox
                            value={item}
                            type='radio'
                            name='season'
                            margin='0 0 15px 0'
                            onChange={setSeason}
                            selectedValues={season}
                            key={String(item + idx)}
                            text={getTranslatedText(`seasonEnum.${item}`)}
                          />
                        ))}
                      </WrapCharacteristic>
                    </Styles.SectionsItem>

                    <Styles.SectionsItem name='size'>
                      <Sizes categories={categoryItems} dimension={{ size, setSize }} />
                    </Styles.SectionsItem>
                  </Styles.WrapItems>
                </>

                <Styles.TextAreaBlock>
                  <Subtitle hiddenStar textTitle={getTranslatedText('addAdv.describeTitle')} />

                  <Input
                    type='textarea'
                    inputGap='20px'
                    name='description'
                    value={description}
                    label={getTranslatedText('addAdv.describeText')}
                    onChange={(e) => setDescription(e.target.value)}
                    error={description.length >= 255 ? 'error' : undefined}
                  />
                </Styles.TextAreaBlock>

                <Location
                  name='locationId'
                  setLocationId={setLocationId}
                  setLocationCurrent={setLocationCurrent}
                  onInputLocation={{ showLocation, setShowLocation }}
                />

                <PhotoFiles
                  name='images'
                  setImages={setImageFiles}
                  preposition={getTranslatedText('addAdv.from')}
                  errorSize={getTranslatedText('popup.sizeFile')}
                  errorTitle={getTranslatedText('popup.errorTitle')}
                  errorAddFile={getTranslatedText('popup.addedFile')}
                  errorRightSize={getTranslatedText('popup.selectFile')}
                  errorNoSaveMore={getTranslatedText('popup.noSaveMore')}
                  description={getTranslatedText('addAdv.uploadDescription')}
                  errorExtension={getTranslatedText('popup.pictureSelection')}
                  photosUploaded={getTranslatedText('addAdv.uploadDescription')}
                  firstUploadText={getTranslatedText('addAdv.firstUploadDescription')}
                />

                <Styles.WrapButtons>
                  <Styles.BlockButtons>
                    <Button
                      width={295}
                      type='submit'
                      colorType='green'
                      icon={<Icon.Plus />}
                      isLoading={isLoading}
                      text={getTranslatedText('button.addAdv')}
                    />

                    <Button
                      type='submit'
                      width={lang === 'ua' ? 270 : 222}
                      onClick={() => setButtonPreview(true)}
                      text={getTranslatedText('addAdv.preview')}
                    />
                  </Styles.BlockButtons>

                  <CancelEverything onClick={resetAll} text={getTranslatedText('addAdv.cancel')} />
                </Styles.WrapButtons>
              </Styles.AddContainer>
            </Styles.Container>
          </Styles.MainContainer>

          <FormikFocus />
        </Form>
      )}
    </FormikHandler>
  );
};

export default AddGoods;
