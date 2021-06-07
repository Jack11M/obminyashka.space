const isValueExistWithoutError = ( value, error ) => {
	return value !== '' && !error;
};


const toggleButtonLog = ( state ) => {
	const { logEmail, logPassword, errors } = state;
	return (
		isValueExistWithoutError( logEmail, errors.length  )
		&&
		isValueExistWithoutError( logPassword, errors.length  )
	);
};

const toggleButtonReg = ( state ) => {
	const { regEmail, regNick, regPassword, regConfirm, regCheckbox, errors } = state;
	return (
		isValueExistWithoutError( regEmail, errors.length )
		&&
		isValueExistWithoutError( regNick, errors.length )
		&&
		isValueExistWithoutError( regPassword, errors.length )
		&&
		isValueExistWithoutError( regConfirm, errors.length)
		&&
		regCheckbox
	);
};

export { toggleButtonLog, toggleButtonReg };
