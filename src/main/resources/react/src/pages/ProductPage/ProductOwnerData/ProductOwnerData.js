import React from 'react';
import './ProductOwnerData.scss';
import ProductDB from '../MokDB';

const ProductOwnerData = () => {
	return (
		<div className = 'productOwnerDataBox'>
			<div className = 'productOwner'>
				<div className = 'productOwnerAvatar'>
					{/*вместо картинки передаётся путь*/}
					<img src = { ProductDB.owner.avatar } alt = 'ownerAva'/>
				</div>
				<div className = 'productOwnerName'>
					<h2>{ ProductDB.owner.name }</h2>
				</div>
			</div>
			<div className = 'postData'>
				<div className = 'postReqData'>
					<ul>
						<li><span>Дата публикации:</span></li>
						<li><span>Город:</span></li>
						<li><span>Телефон:</span></li>
					</ul>
				</div>
				<div className = 'postResData'>
					<ul>
						<li>{ ProductDB.postData.date }</li>
						<li>{ ProductDB.postData.city }</li>
						<li>{ ProductDB.postData.telephone }</li>
					</ul>
				</div>
			</div>
		</div>
	);
};
export default ProductOwnerData;
