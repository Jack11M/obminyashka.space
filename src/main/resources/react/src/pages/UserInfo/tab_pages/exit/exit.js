import React, { useCallback } from 'react';
import { useDispatch, useSelector } from 'react-redux';

import logout2 from '../../../../img/log-out-2.png'
import Button from '../../../../components/button/Button';
import { getTranslatedText } from '../../../../components/local/localisation';
import { postAuthLogoutAsync } from '../../../../redux/auth/action';

import './exit.scss';

const Exit = ( { toggle } ) => {
	const dispatch = useDispatch();
	const { lang } = useSelector( state => state.auth );
	const logOut = useCallback( () => {
		dispatch( postAuthLogoutAsync() );
	}, [ dispatch ] );

	return (
		<div className="modal-overlay">
			<div className="modal">
				<div onClick={ toggle } className="modal__cross js-modal-close"/>
				<p className="modal__title">{ getTranslatedText( 'exit.question', lang ) }</p>
				<p className="modal__text">{ getTranslatedText( 'exit.text', lang ) }</p>
				<Button
					whatClass="button"
					text={ getTranslatedText( 'exit.exit', lang ) }
					width={ '179px' }
					click={ logOut }
				/>
				<div className={ 'background' }>
					<img src={ logout2 } className="log-out-img" alt="log-out"/>
				</div>
			</div>
		</div>
	);
};
export default Exit;
