import React, {useState} from "react";
import {useRouteMatch} from "react-router-dom";

import Tabs from "./tabs";
import RouterTabs from "./tab_pages/router_tabs";
import ActiveProfile from "./active_profile";

import "./profile.scss";

const Profile = () => {
	let {path, url} = useRouteMatch();
	const [aboutMe, setAboutMe] = useState(
		[
			{id: 0, fieldId: 'firstName', label: 'Имя', value: 'Andrey'},
			{id: 1, fieldId: 'lastName', label: 'Фамилия', value: 'Teslenko'},
			{id: 2, fieldId: 'city', label: 'Город', value: 'Kharkov'},
			{id: 3, fieldId: 'phone', label: 'Телефон', value: 'vodafone'}
		]
	)
	const [children, setChildren] = useState([
		{
			id: 0,
			fields: [
				{value: 'Kirill', label: 'Имя', fieldId: 'firstNameChild'},
				{value: 18, fieldId: 'ageChild', label: 'Возраст'}
			],
			addField: true,
			deleteField: false
		},


	])

	return (
		<div className="container">
			<aside className="left-side">
				<ActiveProfile/>
				<Tabs url={url}/>
			</aside>
			<main className="main-content">
				<div className="main-content-wrapper">
					<RouterTabs path={path} Profile={{aboutMe: [aboutMe, setAboutMe], children: [children, setChildren]}}/>
				</div>
			</main>
		</div>
	);
};

export default Profile;
