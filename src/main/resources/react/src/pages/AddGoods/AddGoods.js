import React from "react";

import { useDispatch, useSelector } from 'react-redux';
import CheckBox from "../../components/checkbox/index";
import { getTranslatedText } from '../../components/local/localisation';
import { toggleCheckBox } from '../../redux/auth/action';
import { InputDiv } from "../FormRegister/loginStyle";

import "./AddGoods.scss";



const AddGoods = () => {

	const { lang, logCheckbox } = useSelector(state => state.auth);
	const changeCheckBox = () => {
		dispatch(toggleCheckBox({ logCheckbox: !logCheckbox }));
	};
	const dispatch = useDispatch();
	return (
		<main className="add">
			<div className="add_container">
				<div className="add_inner">
					<div className="add_choose">
						<h3 className="add-title">Выберите раздел</h3>
						<div className="sections">
							<div className="sections_item">
								<h5>* Категория</h5>
								<select>
									<option>Одежда</option>
									<option>Обувь</option>
									<option>Игрушки</option>
								</select>
							</div>
							<div className="sections_item">
								<h5>* Подкатегория</h5>
								<select>
									<option></option>
									<option></option>
									<option></option>
								</select>
							</div>
							<div className="sections_item">
								<h5>* Заголовок обьявления</h5>
								<input type="text"></input>
							</div>
						</div>
					</div>
					<div className=""></div>
					<div className="characteristics">
						<h3> Характеристики</h3>
						<div className="characteristics_items">
							<div className="characteristics_item ">
								<h4>Возраст</h4>
								<CheckBox
									text={'0'}
									margin={'0 0 15px 0'}
									fs={'14px'}
									checked={logCheckbox}
									click={changeCheckBox}
								/>
								<CheckBox
									text={'1-2'}
									margin={'0 0 15px 0'}
									fs={'14px'}
									checked={logCheckbox}
									click={changeCheckBox}
								/>
								<CheckBox
									text={'2-4'}
									margin={'0 0 15px 0'}
									fs={'14px'}
									checked={logCheckbox}
									click={changeCheckBox}
								/>
								<CheckBox
									text={'5-7'}
									margin={'0 0 15px 0'}
									fs={'14px'}
									checked={logCheckbox}
									click={changeCheckBox}
								/>
								<CheckBox
									text={'8-11'}
									margin={'0 0 15px 0'}
									fs={'14px'}
									checked={logCheckbox}
									click={changeCheckBox}
								/>
								<CheckBox
									text={'11-14'}
									margin={'0 0 15px 0'}
									fs={'14px'}
									checked={logCheckbox}
									click={changeCheckBox}
								/>
							</div>
							<div className="characteristics_item">
								<h4>Пол</h4>
								<CheckBox
									text={'Женский'}
									margin={'0 0 15px 0'}
									fs={'14px'}
									checked={logCheckbox}
									click={changeCheckBox}
								/>
								<CheckBox
									text={'Мужской'}
									margin={'0 0 15px 0'}
									fs={'14px'}
									checked={logCheckbox}
									click={changeCheckBox}
								/>
								<CheckBox
									text={'Мальчик/Девочка'}
									margin={'0 0 15px 0'}
									fs={'14px'}
									checked={logCheckbox}
									click={changeCheckBox}
								/>
							</div>
							<div className="characteristics_item">
								<h4>Размер (одежда)</h4>
								<CheckBox
									text={'50 - 80 см'}
									margin={'0 0 15px 0'}
									fs={'14px'}
									checked={logCheckbox}
									click={changeCheckBox}
								/>
								<CheckBox
									text={'80 - 92 см'}
									margin={'0 0 15px 0'}
									fs={'14px'}
									checked={logCheckbox}
									click={changeCheckBox}
								/>
								<CheckBox
									text={'92 - 104 см'}
									margin={'0 0 15px 0'}
									fs={'14px'}
									checked={logCheckbox}
									click={changeCheckBox}
								/>
								<CheckBox
									text={'110 - 122 см'}
									margin={'0 0 15px 0'}
									fs={'14px'}
									checked={logCheckbox}
									click={changeCheckBox}
								/>
								<CheckBox
									text={'128 - 146 см'}
									margin={'0 0 15px 0'}
									fs={'14px'}
									checked={logCheckbox}
									click={changeCheckBox}
								/>
								<CheckBox
									text={'146 - 164 см'}
									margin={'0 0 15px 0'}
									fs={'14px'}
									checked={logCheckbox}
									click={changeCheckBox}
								/>
							</div>
							<div className="characteristics_item">
								<h4>Сезон</h4>
								<CheckBox
									text={'Демисезон'}
									margin={'0 0 15px 0'}
									fs={'14px'}
									checked={logCheckbox}
									click={changeCheckBox}

								/>
								<CheckBox
									text={'Лето'}
									margin={'0 0 15px 0'}
									fs={'14px'}
									checked={logCheckbox}
									click={changeCheckBox}
								/>
								<CheckBox
									text={'Зима'}
									margin={'0 0 15px 0'}
									fs={'14px'}
									checked={logCheckbox}
									click={changeCheckBox}
								/>
							</div>
						</div>

					</div>
					<div className="description">
						<h3 className="description_titile"> Описание</h3>
						<p className="description_subtitile">* Опишите Вашу вещь: деффекты, особенности использования, и пр</p>
						<textarea className="description_textarea"></textarea>
					</div>
					<div className="files">
						<h3>Загрузите фотографии вашей вещи</h3>
						<h5>Первое фото станет обложкой карточки товара</h5>
						<h5>Загружено фотографий 1 из 5</h5>
						<div>
							<input id="file-input1" className = "file-input" type="file" name="file" multiple></input>
								<label for="file-input1" className = "files_label"><span></span></label>
							<input id="file-input1" className = "file-input" type="file" name="file" multiple></input>
								<label for="file-input1" className = "files_label"><span></span></label>
						</div>
						
					</div>

					</div>
				</div>
		</main >
	);
};

export default AddGoods;