import React, {Fragment} from "react";
import "./button.scss";

const Button = ({whatClass, disabling, text}) => {
	return (
		<Fragment>
			<button className={whatClass} disabled={disabling || null}>
				{text}
			</button>
		</Fragment>
	);
};
export default Button;
