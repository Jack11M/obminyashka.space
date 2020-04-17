import React from "react";
import {Link} from "react-router-dom";

const BtnGoodBusiness = props => {
	return (
		<Link to={props.href} className={props.whatClass}>
			<span className="icon-heart"></span>
			{props.text}
		</Link>
	);
};

export default BtnGoodBusiness;
