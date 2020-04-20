import {useEffect, useState} from "react";

export default (key, {isLogin, isChecking}, initialValue = "") => {
	const [value, setValue] = useState(() => {
		let token = "";
		if (isLogin && isChecking) {
			token = localStorage.getItem(key);
		} else {
			token = sessionStorage.getItem(key);
		}
		token = JSON.parse(token);

		return token || initialValue;
	});

	useEffect(() => {
		const valueString = JSON.stringify(value);

		if (isLogin && isChecking) {
			localStorage.setItem(key, valueString);
		} else {
			sessionStorage.setItem(key, valueString);
		}
	}, [value]);

	return [value, setValue];
};
