import React from 'react';
import './ProductOwnerData.scss';

const ProductOwnerData = () => {
	return (
		<div className = 'productOwnerDataBox'>
			<div className = 'productOwner'>
				<div className = 'productOwnerAvatar'>
					<img src = 'https://static.toiimg.com/photo/72975551.cms' alt = 'ownerAva'/>
				</div>
				<div className = 'productOwnerName'>
					<h2>Марина Васильева</h2>
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
						<li>03.09.2019</li>
						<li>Харьков, Холоногорский</li>
						<li>093 546 09 32</li>
					</ul>
				</div>
			</div>
		</div>
	);
};
export default ProductOwnerData;
