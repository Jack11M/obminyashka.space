import React from 'react';

import Avatar from '../../../components/avatar/avatar';

import './ProductOwnerData.scss';

const ProductOwnerData = ( { ava , name , date , city , phone } ) => {

		return (
			<div className = 'productOwnerDataBox'>
				<div className = 'productOwner'>
					<div className = 'productOwnerAvatar'>
						<Avatar height = { 30 } width = { 28 } avatar = { ava }/>
					</div>
					<div className = 'productOwnerName'>
						<h2>{ name }</h2>
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
							<li>{ date }</li>
							<li>{ city }</li>
							<li>{ phone }</li>
						</ul>
					</div>
				</div>
			</div>
		);
	}
;
export default ProductOwnerData;
