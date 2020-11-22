import React, { useCallback } from 'react';
import { useDispatch } from 'react-redux';
import styled from 'styled-components';

import ButtonsAddRemoveChild from './buttonsAddRemoveChild/buttonsAddRemoveChild';
import { addChild, deleteChild } from '../../../redux/profile/profileAction';


const ButtonsBlock = styled.div`
	display: flex;
  justify-content: flex-end;
`;

const AddRemoveDiv = styled.div`
	width: 415px;
	display: flex;
	justify-content: space-between;
`

function BlockButtons( { index, childrenId } ) {
	const dispatch = useDispatch();

	const removeChild = useCallback( () => {
		dispatch( deleteChild( childrenId ) );
	}, [ dispatch, childrenId ] );

	const addChildren = useCallback( () => {
		dispatch( addChild() );
	}, [ dispatch ] );

	return (
		<ButtonsBlock>
			<AddRemoveDiv>
				<ButtonsAddRemoveChild
					text={ 'Добавить поле' }
					addRemove={ 'add' }
					click={ addChildren }
				/>
				{ Boolean( index ) && (
					<ButtonsAddRemoveChild
						text={ 'Удалить поле' }
						addRemove={ 'remove' }
						click={ removeChild }
					/>
				) }
			</AddRemoveDiv>
		</ButtonsBlock>
	);
}

export default BlockButtons;
