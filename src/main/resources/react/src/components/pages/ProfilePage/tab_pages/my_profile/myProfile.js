import React from "react";
import Title_Big_Blue from "../../../../title_Big_Blue";
import InputData from '../../inputData';
import InputChildren from "../../inputChildren";

import './myProfile.scss';

const MyProfile = ({Profile}) => {
	const {aboutMe, children} = Profile;
	const[me, setMe]=aboutMe;
	const[child, setChild]= children;

	return (
		<form>
			<Title_Big_Blue whatClass={'myProfile-title'} text={"О себе"}/>
			{me.map(input => {
				return (<InputData data={input} key={input.id}/>)
			})}

			<Title_Big_Blue whatClass={'myProfile-title'} text={"Дети"}/>
			{child.map(item=>(<InputChildren data={item} key={item.id}/>))}

		</form>
	);
};

export default MyProfile;
