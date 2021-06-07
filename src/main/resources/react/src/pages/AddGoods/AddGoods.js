import React from "react";

import { useDispatch, useSelector } from 'react-redux';
import CheckBox from "../../components/common/checkbox/index";
import { toggleCheckBox } from '../../redux/auth/action';
import { books, clothes, furniture, kidsUpToYear, other, shoes, toys, transportForChildren } from '../../img/all_images_export/navItems';
import ButtonAdv from "../../components/common/buttonAdv/ButtonAdv";
import Button from "../../components/common/button/Button";

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
								<h5 className="sections_item-description">* Категория</h5>
								<div className="select">
									<img src={clothes} alt="clothes" />
									<p>Одежда</p>
								</div>
							</div>
							<div className="sections_item">
								<h5 className="sections_item-description">* Подкатегория</h5>
								<div className="select">
									<p>Колготки, носки</p>
								</div>
							</div>
							<div className="sections_item">
								<h5 className="sections_item-description">* Заголовок обьявления</h5>
								<input type="text"></input>
							</div>
						</div>
					</div>
					<div className="change">
						<h3 className="change_title">Обмен</h3>
						<p className="change-description">* На что хотите обменяться?</p>
						<p className="change-description">(введите фразу, а потом нажмите Enter)</p>
						<div className="change_wrapper">
							<div className="change_item">Ботинки зимние <span></span></div>
							<div className="change_item">Кофта на девочку 7 лет<span></span></div>
							<div className="change_input-wrapper" >
								<input className="change_input" type="text" placeholder="Введите на что хотите обменяться"></input>
							</div>
						</div>
						<CheckBox
									text={'Рассмотрю Ваше предложение'}
									margin={'0 0 15px 0'}
									fs={'14px'}
									checked={logCheckbox}
									click={changeCheckBox}
								/>
					</div>
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
						<p>Первое фото станет обложкой карточки товара</p>
						<p>Загружено фотографий 1 из 5</p>
						<div className = "files_wrapper">
							<input id="file-input1" className = "file_input" type="file" name="file" multiple></input>
								<label for="file-input1" className = "files_label added"><span></span></label>
							<input id="file-input2" className = "file_input " type="file" name="file" multiple></input>
								<label for="file-input1" className = "files_label "><span></span></label>
						</div>
						
					</div>
					<div className="bottom_block">
						<div className="buttons_block">
							<ButtonAdv/>
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
				
		</main >
	);
};

export default AddGoods;
