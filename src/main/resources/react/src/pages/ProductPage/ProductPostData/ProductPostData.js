import React from 'react';
import './ProductPostData.scss';
import Button from '../../../components/button/Button';
import { ProductDB } from '../MokDB';

const ProductPostData = () => {
	return (
		<div className = 'productPostData'>
			<div className = 'postHeading'>
				<h2>{ ProductDB.productData.nameItem }</h2>
			</div>
			<div className = 'postDataDescription'>
				<p><span>Меняет на:</span></p>
				<ol>
					{ProductDB.productData.forChange.map((item, idx) =>
						<li key={`li_${idx}`}>{item}</li>
					)}
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
						<li>{ ProductDB.productData.specification.size_age }</li>
						<li>{ ProductDB.productData.specification.stateProduct }</li>
						<li>{ ProductDB.productData.specification.season }</li>
						<li>{ ProductDB.productData.specification.sex }</li>
					</ul>
				</div>
			</div>
		</div>
	);
};
export default ProductPostData;