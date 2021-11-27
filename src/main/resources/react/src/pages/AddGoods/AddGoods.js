import React, { useContext, useState } from 'react';
import { Form } from 'formik';
import { useSelector } from 'react-redux';

import api from 'REST/Resources';
import { enumAge } from 'config/ENUM.js';
import { ModalContext } from 'components/common/pop-up';
import Button from 'components/common/buttons/button/Button';
import { getTranslatedText } from 'components/local/localisation';
import ButtonAdv from 'components/common/buttons/buttonAdv/ButtonAdv';
import { FormHandler, FormikCheckBox } from 'components/common/formik';
import { sendNewAdv } from 'REST/Resources/fetchAddGood';

import { Sizes } from './sizes';
import { Location } from './location';
import { Exchange } from './exchange';
import { convertToMB } from './add-image/helper';
import { ImagePhoto } from './add-image/image-photo';
import { SelectionSection } from './selection-section';
import { AddFileInput } from './add-image/add-file-input';

import './AddGoods.scss';

const AddGoods = () => {
  const { openModal } = useContext(ModalContext);
  const { lang } = useSelector((state) => state.auth);

  const [announcementTitle, setAnnouncementTitle] = useState('');
  const [exchangeList, setExchangeList] = useState([]);
  const [description, setDescription] = useState('');
  const [locationId, setLocationId] = useState(null);
  const [imageFiles, setImageFiles] = useState([]);

  const [categoryItems, setCategoryItems] = useState('');
  const [subCategoryItems, setSubCategoryItems] = useState('');
  const [size, setSize] = useState('');
  const [locationCurrent, setLocationCurrent] = useState(null);

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

  const handleSubmit = async (values) => {
    console.log(values);
    const { images, ...rest } = values;

    console.log(images, rest);

    let data = new FormData();

    //
    data.append('dto', JSON.stringify(rest));
    // data.append()
    images.forEach((item) => data.append('image', item));

    // const arr = Object.entries(rest);
    //
    // arr.forEach(([key, value]) => {
    //   console.log( {key, value});
    //   data.append(key, value);
    // });
    try {
      const a = await api.fetchAddGood.sendNewAdv(data);
      console.log(a);
    } catch (e) {
      console.log(e.response);
    }
  };

  const initialValues = {
    id: 0,
    dealType: 'EXCHANGE',
    categoryId: categoryItems?.id,
    subcategoryId: subCategoryItems?.id,
    topic: announcementTitle,
    readyForOffers: false,
    wishesToExchange: exchangeList.join(','),
    age: '',
    gender: '',
    season: '',
    size: size,
    description: description,
    locationId: locationId,
    images: imageFiles,
  };
  const ages = Object.keys(enumAge);
  const sex = ['FEMALE', 'MALE', 'UNSELECTED'];
  const season = ['ALL_SEASONS', 'DEMI_SEASON', 'SUMMER', 'WINTER'];

  return (
    <FormHandler
      onSubmit={handleSubmit}
      initialValues={initialValues}
      // validationSchema={createSchema}
    >
      <Form>
        <main className="add">
          <div className="add_container">
            <div className="add_inner">
              <SelectionSection
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
                    {ages.map((item, idx) => (
                      <FormikCheckBox
                        key={item + idx}
                        text={enumAge[item]}
                        value={item}
                        name="age"
                        type="radio"
                        margin="0 0 15px -7px"
                      />
                    ))}
                  </div>
                  <div className="characteristics_item">
                    <h4>{getTranslatedText(`addAdv.sex`, lang)}:</h4>
                    {sex.map((item, idx) => (
                      <FormikCheckBox
                        key={item + idx}
                        text={getTranslatedText(`genderEnum.${item}`, lang)}
                        value={item}
                        name="gender"
                        type="radio"
                        margin="0 0 15px -7px"
                      />
                    ))}
                  </div>

                  <div className="characteristics_item">
                    <h4>{getTranslatedText(`addAdv.season`, lang)}:</h4>
                    {season.map((item, idx) => (
                      <FormikCheckBox
                        key={item + idx}
                        text={getTranslatedText(`seasonEnum.${item}`, lang)}
                        value={item}
                        name="season"
                        type="radio"
                        margin="0 0 15px -7px"
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
                  className="description_textarea"
                  onChange={(e) => setDescription(e.target.value)}
                />
              </div>

              <Location
                setLocationId={setLocationId}
                setLocationCurrent={setLocationCurrent}
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
                    text="Предпросмотр"
                    width="222px"
                  />
                </div>
                <div className="cancel">
                  <p>Отменить все</p>
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
