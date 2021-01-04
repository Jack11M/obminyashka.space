const isValueExistWithoutError = ( data ) => {
	return data.value !== '' && data.error === '';
};


export const toggleButtonLog = ( state ) => {
	const { logEmail, logPassword } = state;
	return (isValueExistWithoutError( logEmail ) && isValueExistWithoutError( logPassword ));
};

export const toggleButtonReg = ( state ) => {
	const { regEmail, regNick, regPassword, regConfirm, regCheckbox } = state;
	return (isValueExistWithoutError( regEmail ) && isValueExistWithoutError( regNick ) && isValueExistWithoutError( regPassword ) && isValueExistWithoutError( regConfirm ) && regCheckbox);
};
