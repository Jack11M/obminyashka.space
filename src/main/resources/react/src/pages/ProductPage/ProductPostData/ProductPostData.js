import React from 'react';
import './ProductPostData.scss';
import Button from '../../../components/button/Button';

const ProductPostData = () => {
	return (
		<div className = 'productPostData'>
			<div className = 'postHeading'>
				<h2>Кофта детская с кроликом</h2>
			</div>
			<div className = 'postDataDescription'>
				<p><span>Меняет на:</span></p>
				<ol>
					<li>ботинки зимние 250 мм на девочку</li>
					<li>колготки 120см</li>
					<li>шапочка для басейна</li>
					<li>кофта для 7 лет</li>
					<li>ваше предложение</li>
				</ol>
			</div>
			<div className = 'postButton'>
				<Button
					text = "Предложить обмен"
					width = { '250px' }/>
			</div>
			<div className = 'postHeading'>
				<h2>Описание:</h2>
			</div>
			<div className = 'postDataBox'>
				<div className = 'postDataDescription'>
					<ul>
						<li><span>Размер/возраст:</span></li>
						<li><span>Состояние:</span></li>
						<li><span>Сезон:</span></li>
						<li><span>Пол:</span></li>
					</ul>
				</div>
				<div className = 'postDataDescription'>
					<ul>
						<li>110 см/5 лет</li>
						<li>Б/у</li>
						<li>Осень/Зима</li>
						<li>Унисекс</li>
					</ul>
				</div>
			</div>
		</div>
	);
};
export default ProductPostData;