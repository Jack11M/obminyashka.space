import React, { useContext, useState } from 'react';
import { Form } from 'formik';
import { useSelector } from 'react-redux';

import { enumAge } from 'config/ENUM.js';
import { ModalContext } from 'components/common/pop-up';
import Button from 'components/common/buttons/button/Button';
import { getTranslatedText } from 'components/local/localisation';
import ButtonAdv from 'components/common/buttons/buttonAdv/ButtonAdv';
import { FormHandler, FormikCheckBox } from 'components/common/formik';

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

  const [categoryId, setCategoryId] = useState('');
  const [subCategoryId, setSubCategoryId] = useState('');
  const [announcementTitle, setAnnouncementTitle] = useState('');
  const [exchangeList, setExchangeList] = useState([]);
  const [locationId, setLocationId] = useState(null);
  const [imageFiles, setImageFiles] = useState([]);

  const [categoryItems, setCategoryItems] = useState('');
  const [subCategoryItems, setSubCategoryItems] = useState('');
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
              Файл который Вы добавляете, уже существует.
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
              Пожалуйста, выберите картинку с расширением ( jpg, jpeg, png ).
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
              {`Размер вашего файла ${valueString}, `}
              <br /> Выберите файл меньше 10 МБ.
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
              Вы не можете сохранить больше 10 файлов.
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

  const handleSubmit = (values) => {
    console.log(values);
    return values;
  };

  const initialValues = {
    id: 0,
    dealType: 'EXCHANGE',
    categoryId: null,
    subcategoryId: null,
    topic: announcementTitle,
    readyForOffers: false,
    wishesToExchange: exchangeList.join(','),
    age: '',
    gender: '',
    season: '',
    size: '',
    description: '',
    locationId: locationId,
    images: imageFiles,
  };
  const ages = Object.keys(enumAge);
  const sex = ['FEMALE', 'MALE', 'UNSELECTED'];
  const season = ['ALL_SEASONS', 'DEMI_SEASON', 'SUMMER', 'WINTER'];
  const size = ['50-80', '80-92', '92-104', '110-122', '128-146', '146-164'];
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
                    <h4>{getTranslatedText(`addAdv.size`, lang)}:</h4>
                    {size.map((item, idx) => (
                      <FormikCheckBox
                        key={item + idx}
                        text={`${item} см`}
                        value={item}
                        name="size"
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
                </div>
              </div>
              <div className="description">
                <h3 className="description_title"> Описание</h3>
                <p className="description_subtitle">
                  <span className="span_star">*</span> Опишите Вашу вещь:
                  деффекты, особенности использования, и пр
                </p>
                <textarea className="description_textarea" />
              </div>

              <Location
                setLocationId={setLocationId}
                setLocationCurrent={setLocationCurrent}
              />

              <div className="files">
                <h3>Загрузите фотографии ваших вещей</h3>
                <p>Первое фото станет обложкой карточки товара</p>
                <p>Загружено фотографий {imageFiles.length} из 10</p>
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
