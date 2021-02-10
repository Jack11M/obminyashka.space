import React from 'react';
import { useSelector } from 'react-redux';
import { useHistory } from 'react-router-dom';
import Button from '../../components/button/Button';
import { getTranslatedText } from '../../components/local/localisation';
import { greenDots, loop, orangeDots, somethingBad } from '../../img/all_images_export/errorPage';
import { route } from '../../routes/routeConstants';

import './somethingBad.scss';

const SomethingBad = ( { deactivateError } ) => {
	const { lang } = useSelector( state => state.auth );
	let history = useHistory();

	const goTo = ( event ) => {
		deactivateError( false );
		if (event.target.className.includes( 'onMain' )) {
			history.push( route.home );
		} else {
			history.goBack();
		}
	};

	return (
		<div className={ 'error-page' }>
			<div className={ 'blockCenterImage' }>
				<div className={ 'blockCenterImage-somethingBad' }>
					<img src={ somethingBad } alt={ 'somethingBad' }/>
				</div>
			</div>
			<div className={ 'blockOrangeImage' }>
				<img src={ orangeDots } alt={ 'orange dots' }/>
			</div>
			<div className={ 'blockGreenImage' }>
				<img src={ greenDots } alt={ 'green dots' }/>
			</div>
			<div className={ 'blockRightImage' }>
				<img src={ loop } alt={ 'loop' }/>
			</div>
			<h2>{ getTranslatedText( 'somethingBad.error', lang ) }</h2>
			<div className={ 'blockButtons' }>
				<Button whatClass={ 'onMain' } text={ getTranslatedText( 'fourOhFour.mainPage', lang ) } click={ goTo }/>
				<Button whatClass={ 'back' } text={ getTranslatedText( 'fourOhFour.backPage', lang ) } click={ goTo }/>
			</div>
		</div>
	);
};
export default SomethingBad;
