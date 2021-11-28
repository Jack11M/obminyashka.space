import React, { useContext, useState } from 'react';
import { Form } from 'formik';
import * as Yup from 'yup';
import { useSelector } from 'react-redux';

import api from 'REST/Resources';
import ru from 'components/local/ru';
import { enumAge } from 'config/ENUM.js';
import { ModalContext } from 'components/common/pop-up';
import Button from 'components/common/buttons/button/Button';
import { getTranslatedText } from 'components/local/localisation';
import ButtonAdv from 'components/common/buttons/buttonAdv/ButtonAdv';
import { FormHandler, FormikCheckBox } from 'components/common/formik';

import { Sizes } from './sizes';
import { Location } from './location';
import { Exchange } from './exchange';
import { convertToMB } from './add-image/helper';
import { ImagePhoto } from './add-image/image-photo';
import { SelectionSection } from './selection-section';
import { AddFileInput } from './add-image/add-file-input';

import './AddGoods.scss';

const AddGoods = () => {
  const { genderEnum, seasonEnum } = ru;
  const { openModal } = useContext(ModalContext);
  const { lang } = useSelector((state) => state.auth);

  const [announcementTitle, setAnnouncementTitle] = useState('');
  const [exchangeList, setExchangeList] = useState([]);
  const [description, setDescription] = useState('');
  const [locationId, setLocationId] = useState(null);
  const [imageFiles, setImageFiles] = useState([]);

  const [categoryItems, setCategoryItems] = useState('');
  const [subCategoryItems, setSubCategoryItems] = useState('');
  const [readyOffer, setReadyOffer] = useState([]);
  const [age, setAge] = useState([]);
  const [gender, setGender] = useState([]);
  const [season, setSeason] = useState([]);
  const [size, setSize] = useState('');
  const [locationCurrent, setLocationCurrent] = useState(null);
  const [showLocation, setShowLocation] = useState({
    city: '',
    area: '',
  });

  const [preViewImage, setPreViewImage] = useState([]);
  const [currentIndexImage, setCurrentIndexImage] = useState(null);

  const filesAddHandler = (event, dropFiles = null) => {
    event.preventDefault();

    const files = Array.from(dropFiles || event.target.files);

    files.forEach((file, index, iterableArray) => {
      const notAbilityToDownload =
        10 - imageFiles.length - iterableArray.length < 0;

      const foundSameFile = imageFiles.filter(
        (image) => image.name === file.name
      );

      if (foundSameFile.length) {
        openModal({
          title: getTranslatedText('popup.errorTitle', lang),
          children: (
            <p style={{ textAlign: 'center' }}>
              {getTranslatedText('popup.addedFile', lang)}
            </p>
          ),
        });
        return;
      }

      if (!file.type.match('image') || file.type.match('image/svg')) {
        openModal({
          title: getTranslatedText('popup.errorTitle', lang),
          children: (
            <p style={{ textAlign: 'center' }}>
              {getTranslatedText('popup.pictureSelection', lang)} ( jpg, jpeg,
              png, git ).
            </p>
          ),
        });
        return;
      }

      const { value, valueString } = convertToMB(file.size);
      if (value >= 10 && valueString.includes('MB')) {
        openModal({
          title: getTranslatedText('popup.errorTitle', lang),
          children: (
            <p style={{ textAlign: 'center' }}>
              {getTranslatedText('popup.sizeFile', lang)} {valueString}
              <br /> {getTranslatedText('popup.selectFile', lang)}
            </p>
          ),
        });
        return;
      }
      if (notAbilityToDownload) {
        openModal({
          title: getTranslatedText('popup.errorTitle', lang),
          children: (
            <p style={{ textAlign: 'center' }}>
              {getTranslatedText('popup.noSaveMore', lang)}
            </p>
          ),
        });
        return;
      }

      const reader = new FileReader();
      reader.readAsDataURL(file);
      reader.onload = (event) => {
        if (event.target.readyState === 2) {
          setPreViewImage((prev) => [...prev, event.target.result]);
          setImageFiles((prev) => [...prev, file]);
        }
      };
    });
  };

  const removeImage = (event, index) => {
    event.preventDefault();
    const newImageFiles = [...imageFiles];
    const newPreViewImage = [...preViewImage];
    newPreViewImage.splice(index, 1);
    newImageFiles.splice(index, 1);
    setImageFiles(newImageFiles);
    setPreViewImage(newPreViewImage);
  };

  const dragStartHandler = (e, index) => {
    setCurrentIndexImage(index);
  };

  const dragEndHandler = (e) => {
    e.target.style.background = 'white';
  };

  const dragOverHandler = (e) => {
    e.preventDefault();
    e.target.style.background = 'lightgrey';
  };

  const changeStateForImagesWhenDrop = (
    processedArray,
    setProcessedArray,
    index
  ) => {
    const newPrevArr = [...processedArray];
    const underPrevImage = newPrevArr[index];
    const currentPrevImage = newPrevArr[currentIndexImage];
    newPrevArr[currentIndexImage] = underPrevImage;
    newPrevArr[index] = currentPrevImage;
    setProcessedArray(newPrevArr);
  };

  const dropHandler = (e, index) => {
    e.preventDefault();
    e.target.style.background = 'white';
    changeStateForImagesWhenDrop(preViewImage, setPreViewImage, index);
    changeStateForImagesWhenDrop(imageFiles, setImageFiles, index);
  };

  const validationAdv = Yup.object().shape({
    id: Yup.number().default(() => 0),
    dealType: Yup.string().default(() => 'EXCHANGE'),
    categoryId: Yup.number().default(() => categoryItems?.id),
    subcategoryId: Yup.number().default(() => subCategoryItems?.id),
    topic: Yup.string()
      .min(3, () => console.log('topic 3'))
      .max(30, () => console.log('topic 70'))
      .default(() => announcementTitle),
    readyForOffers: Yup.boolean().default(() => !!readyOffer.length),
    wishesToExchange: Yup.string()
      .required(() => console.log('required'))
      .default(() => exchangeList.join(',')),
    age: Yup.string().default(() => age.join('')),
    gender: Yup.string().default(() => gender.join('')),
    season: Yup.string().default(() => season.join('')),
    size: Yup.string().default(() => size),
    description: Yup.string().max(255, () => console.log('description')).default(() => description),
    locationId: Yup.string().default(() => locationId),
    images: Yup.array()
      .nullable()
      .default(() => imageFiles),
  });

  const handleSubmit = async (values) => {
    const { images, ...rest } = values;
    console.log(values);
    let data = new FormData();
    data.append('dto', JSON.stringify(rest));

    images.forEach((item) => data.append('image', item));

    try {
      const a = await api.fetchAddGood.sendNewAdv(data);
      console.log(a);
    } catch (err) {
      console.log(err.response.data);
    }
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
                    <h4>{getTranslatedText(`addAdv.age`, lang)}:</h4>
                    {agesShow.map((item, idx) => (
                      <FormikCheckBox
                        key={item + idx}
                        text={enumAge[item]}
                        value={item}
                        name="age"
                        type="radio"
                        margin="0 0 15px -7px"
                        onChange={setAge}
                        selectedValues={age}
                      />
                    ))}
                  </div>
                  <div className="characteristics_item">
                    <h4>{getTranslatedText(`addAdv.sex`, lang)}:</h4>
                    {sexShow.map((item, idx) => (
                      <FormikCheckBox
                        key={item + idx}
                        value={item}
                        name="gender"
                        type="radio"
                        margin="0 0 15px -7px"
                        onChange={setGender}
                        selectedValues={gender}
                        text={getTranslatedText(`genderEnum.${item}`, lang)}
                      />
                    ))}
                  </div>

                  <div className="characteristics_item">
                    <h4>{getTranslatedText(`addAdv.season`, lang)}:</h4>
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

              <div className="files">
                <h3>{getTranslatedText(`addAdv.uploadDescription`, lang)}</h3>
                <p>
                  {getTranslatedText(`addAdv.firstUploadDescription`, lang)}
                </p>
                <p>
                  {getTranslatedText(`addAdv.photosUploaded`, lang)}{' '}
                  {imageFiles.length} {getTranslatedText(`addAdv.from`, lang)}{' '}
                  10
                </p>
                <div className="files_wrapper">
                  {preViewImage.map((url, index) => (
                    <ImagePhoto
                      key={index}
                      url={url}
                      index={index}
                      onDragStart={(e) => dragStartHandler(e, index)}
                      onDragLeave={(e) => dragEndHandler(e)}
                      onDragEnd={(e) => dragEndHandler(e)}
                      onDragOver={(e) => dragOverHandler(e)}
                      onDrop={(e) => dropHandler(e, index)}
                      removeImage={removeImage}
                    />
                  ))}

                  {imageFiles.length < 10 && (
                    <AddFileInput onChange={filesAddHandler} />
                  )}
                </div>
              </div>
              <div className="bottom_block">
                <div className="buttons_block">
                  <ButtonAdv type="submit" />
                  <Button
                    whatClass="preview"
                    text={getTranslatedText(`addAdv.preview`, lang)}
                    width={lang === 'ua' ? '270px' : '222px'}
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
