import React, { useCallback } from "react";
import { useDispatch, useSelector } from 'react-redux';

import TitleBigBlue from "../../../../components/title_Big_Blue";
import InputProfile from "../../components/inputProfile/inputProfile";
import BlockButtons from "../../components/blockButtons";
import Button from "../../../../components/button/Button";


import "./myProfile.scss";
import "./buttonForProfile.scss";
import { addMeInputValue,addChildrenInputValue } from '../../../../redux/actions/profile';

const MyProfile = () => {
	const dispatch = useDispatch();
  const { me, children } = useSelector(state => state.profileMe);


	const addInputMe = useCallback((e) => {
		dispatch(addMeInputValue({[e.target.name]: e.target.value}))
	},[dispatch])
	const addInputChildren = useCallback((e, id) => {
		dispatch(addChildrenInputValue({[e.target.name]: e.target.value}, id))
	}, [dispatch])

	return (
		<form>
			<TitleBigBlue whatClass={"myProfile-title"} text={"О себе"}/>
			{me.map((input, idx) => {
				return <InputProfile data={input} key={`${input.name}_${idx}`} value={me.value} click={addInputMe}/>;
			})}
			<div className={'block-children'}>
				<TitleBigBlue whatClass={"myProfile-title"} text={"Дети"}/>
				{children.map((item, idx) => {
					return (
						<div className={"block-child"} key={`${item.nameChild.name}_${idx}`}>
							<InputProfile data={item.nameChild} value={children.value} click={(e)=>addInputChildren(e, item.id)}/>
							<InputProfile data={item.ageChild} value={children.value} click={(e)=>addInputChildren(e, item.id)}/>
							<BlockButtons index={idx} childrenId={item.id}/>
						</div>
					);
				})}
			</div>
			<Button text={"Сохранить"} whatClass={"btn-profile"}/>
		</form>
	);
};

export default MyProfile;
