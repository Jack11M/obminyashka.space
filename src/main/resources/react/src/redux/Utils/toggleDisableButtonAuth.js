export const toggleButtonLog = ( state ) => {
	const { logEmail, logPassword } = state;
	return (logEmail.value !== '' && logEmail.error === '' && logPassword.value !== '' && logPassword.error === '');
};

export const toggleButtonReg = ( state ) => {
	const { regEmail, regNick, regPassword, regConfirm, regCheckbox } = state;
	return (regEmail.value !== '' && regEmail.error === '' && regNick.value !== '' && regNick.error === '' && regPassword.value !== '' && regPassword.error === '' && regConfirm.value !== '' && regConfirm.error === '' && regCheckbox);
};
