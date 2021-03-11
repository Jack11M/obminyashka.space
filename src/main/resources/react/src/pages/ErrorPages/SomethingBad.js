import React from 'react';
import { useSelector } from 'react-redux';
import { useHistory } from 'react-router-dom';
import Button from '../../components/button/Button';
import { getTranslatedText } from '../../components/local/localisation';
import { somethingBad, greenDots, loop, orangeDots } from '../../img/all_images_export/errorPage';

import './somethingBad.scss';

const SomethingBad = ({deactivateError}) => {
	const { lang } =useSelector(state => state.auth)
	let history = useHistory();

	const goTo = (event) => {
		deactivateError(false )
		if(event.target.className.includes("onMain") ){
			history.push( '/' )
		} else {
			history.goBack()
		}
	};

	return (
		<div className={ 'somethingBad' }> 
			
			<div className={ 'blockOrangeImage' }>
				<img src={ orangeDots } alt={ 'orange dots' }/>
			</div>
			<div className={ 'blockGreenImage' }>
				<img src={ greenDots } alt={ 'green dots' }/>
			</div>
			<div className={ 'blockRightImage' }>
				<img src={ loop } alt={ 'loop' }/>
			</div>
			<div className={ 'blockControls' }>
				<h2>{ getTranslatedText('somethingBad.error', lang) }</h2>
				<div className={ 'blockButtons' }>
					<Button whatClass={ 'onMain' } text={ getTranslatedText('fourOhFour.mainPage', lang) } click={ goTo }/>
					<Button whatClass={ 'back' } text={ getTranslatedText('fourOhFour.backPage', lang) } click={ goTo }/>
			</div>
			</div>
			
		</div>
	);
};
export default SomethingBad;
