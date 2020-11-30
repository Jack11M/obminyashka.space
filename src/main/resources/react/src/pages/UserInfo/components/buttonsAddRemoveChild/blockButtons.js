import React, { useCallback } from 'react';
import styled from 'styled-components';

import ButtonsAddRemoveChild from './buttonsAddRemoveChild';


const ButtonsBlock = styled.div`
	display: flex;
  justify-content: flex-end;
  margin-bottom: ${ props => props.mb };
`;

const AddRemoveDiv = styled.div`
	width: 415px;
	display: flex;
	justify-content: space-between;
`;

function BlockButtons( { mb, index, id, add, remove } ) {

	const itemAdd = useCallback( () => {
		add();
	}, [ add ] );

	const itemRemove = useCallback( () => {
		remove( id );
	}, [ remove, id ] );

	return (
		<ButtonsBlock mb={ mb }>
			<AddRemoveDiv>
				<ButtonsAddRemoveChild
					text={ 'Добавить поле' }
					addRemove={ 'add' }
					click={ itemAdd }
				/>
				{ Boolean( index ) && (
					<ButtonsAddRemoveChild
						text={ 'Удалить поле' }
						addRemove={ 'remove' }
						click={ itemRemove }
					/>
				) }
			</AddRemoveDiv>
		</ButtonsBlock>
	);
}

export default BlockButtons;
