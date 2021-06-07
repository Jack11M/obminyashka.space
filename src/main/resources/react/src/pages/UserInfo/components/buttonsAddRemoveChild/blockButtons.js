import React, { useCallback } from 'react';
import { useSelector } from 'react-redux';
import styled from 'styled-components';

import ButtonsAddRemoveChild from './buttonsAddRemoveChild';
import { getTranslatedText } from '../../../../components/local/localisation';

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
	const { lang } = useSelector( state => state.auth );
	const { children, receivedChildrenFromBack } = useSelector( state => state.profileMe);
	const itemAdd = useCallback( () => {
		add();
	}, [ add ] );

	const itemRemove = useCallback( () => {
		const localDelete = children.length > receivedChildrenFromBack.length
		remove( index, id , localDelete );
	}, [ index, remove, id, children.length, receivedChildrenFromBack.length ] );

	return (
		<ButtonsBlock mb={ mb }>
			<AddRemoveDiv>
				<ButtonsAddRemoveChild
					text={ getTranslatedText( 'button.addField', lang ) }
					addRemove={ 'add' }
					click={ itemAdd }
				/>
				{ Boolean( index ) && (
					<ButtonsAddRemoveChild
						text={ getTranslatedText( 'button.removeField', lang ) }
						addRemove={ 'remove' }
						click={ itemRemove }
					/>
				) }
			</AddRemoveDiv>
		</ButtonsBlock>
	);
}

export default BlockButtons;
