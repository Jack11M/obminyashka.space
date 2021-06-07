import React, { useCallback } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { useHistory } from 'react-router-dom';

import logout2 from '../../../../img/log-out-2.png';
import Button from '../../../../components/common/button/Button';
import { getTranslatedText } from '../../../../components/local/localisation';
import { fetchLogOut } from '../../../../store/auth/slice';
import { route } from '../../../../routes/routeConstants';

import './exit.scss';

const Exit = ( { toggle } ) => {
	const history = useHistory();
	const dispatch = useDispatch();
	const { lang } = useSelector( state => state.auth );
	const setLogOut = useCallback( () => {
		dispatch( fetchLogOut() );
		history.push( route.home );
	}, [ dispatch, history ] );

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
					click={ setLogOut }
				/>
				<div className={ 'background' }>
					<img src={ logout2 } className="log-out-img" alt="log-out"/>
				</div>
			</div>
		</div>
	);
};
export default Exit;
