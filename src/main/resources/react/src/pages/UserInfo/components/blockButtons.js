import React, { useCallback } from "react";
import { useDispatch } from 'react-redux';

import ButtonsAddRemoveChild from './buttonsAddRemoveChild/buttonsAddRemoveChild';
import { addChild, deleteChild } from '../../../redux/actions/profile';


function BlockButtons({index, childrenId}) {
	const dispatch = useDispatch();

	const removeChild = useCallback(() => {
		dispatch(deleteChild(childrenId))
	}, [dispatch]);

	const addChildren = useCallback(() => {
		dispatch(addChild())
	}, [dispatch]);

	return (
		<div className={"block-buttons"}>
			<div className={"block-buttons__add-remove"}>
				<ButtonsAddRemoveChild whatIsClass={"block-buttons__add-remove-item"} text={'Добавить поле'} classSpan={"add"}
					click={addChildren}/>
				{Boolean(index) && (
					<ButtonsAddRemoveChild whatIsClass={"block-buttons__add-remove-item"} text={'Удалить поле'} classSpan={"remove"}
						click={removeChild}/>
				)}
			</div>
		</div>
	);
}

export default BlockButtons;
