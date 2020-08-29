import React from 'react';

const ButtonsAddRemoveChild = (props) => {
	const {whatIsClass, text, classSpan, click} = props;
	return (
		<div className={whatIsClass}>
			<span>{text}</span> <span className={classSpan} onClick={click}/>
		</div>
	);
}

export default ButtonsAddRemoveChild;