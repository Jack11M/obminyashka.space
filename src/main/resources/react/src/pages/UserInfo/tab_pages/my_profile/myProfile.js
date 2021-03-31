import React, { useCallback, useEffect } from 'react';
import { useDispatch, useSelector } from 'react-redux';

import TitleBigBlue from '../../../../components/title_Big_Blue';
import InputProfile from '../../components/inputProfile/inputProfile';
import BlockButtons from '../../components/buttonsAddRemoveChild/blockButtons';
import Button from '../../../../components/button/Button';
import InputGender from '../../components/inputProfile/inputGender';
import {
	addChild,
	addPhone,
	changeChildrenInputValue,
	changeInputValue,
	changePermissionChildren,
	changePermissionProfile,
	changePhoneInputValue,
	chooseGender,
	deleteChild,
	deleteChildrenAsync,
	deletePhone,
	fetchUserInfoAsync,
	postChildrenAsync,
	putChildrenAsync,
	putUserInfoAsync
} from '../../../../redux/profile/profileAction';

import { getTranslatedText } from '../../../../components/local/localisation';

import './myProfile.scss';


const MyProfile = () => {
	const dispatch = useDispatch();
	const { lang } = useSelector( state => state.auth );
	const {
		firstName,
		lastName,
		phones,
		mockPhones,
		children,
		mockChildren,
		prohibitionToSendAboutMe,
		prohibitionToSendChildren,
		receivedChildrenFromBack
	} = useSelector( state => state.profileMe );

	const phonesArray = !phones.length ? mockPhones : phones;
	const childrenArray = !children.length ? mockChildren : children;


	useEffect( () => {
		dispatch( fetchUserInfoAsync() );
	}, [ dispatch ] );

	useEffect( () => {
		dispatch( changePermissionProfile() );
	}, [ dispatch, firstName, lastName, phones ] );

	useEffect( () => {
		dispatch( changePermissionChildren() );
	}, [ dispatch, children ] );

	const changeInput = useCallback( ( event ) => {
		dispatch( changeInputValue( [ event.target.name, event.target.value ] ) );
	}, [ dispatch ] );

	const changeInputPhone = useCallback( ( event, id ) => {
		dispatch( changePhoneInputValue( [ event.target.name, event.target.value ], id ) );
	}, [ dispatch ] );

	const phoneAdd = useCallback( () => {
		dispatch( addPhone() );
	}, [ dispatch ] );

	const phoneRemove = useCallback( () => {
		dispatch( deletePhone() );
	}, [ dispatch ] );

	const changeInputChildren = useCallback( ( event, id ) => {
		dispatch( changeChildrenInputValue( [ event.target.name, event.target.value ], id ) );
	}, [ dispatch ] );

	const pickBoyOrGirl = useCallback( ( id, gender ) => {
		dispatch( chooseGender( gender, id ) );
	}, [ dispatch ] );


	const childAdd = useCallback( () => {
		dispatch( addChild() );
	}, [ dispatch ] );


	const childDelete = useCallback( ( idx, childrenId, localDelete ) => {
		localDelete
			? deleteChild( [ idx, childrenId, localDelete ] )
			: dispatch( deleteChildrenAsync( [ idx, childrenId ] ) );
	}, [ dispatch ] );

	const submitFormAboutMe = ( e ) => {
		e.preventDefault();
		dispatch( putUserInfoAsync( { firstName, lastName, phones } ) );
	};

	const submitFormChildren = ( e ) => {
		e.preventDefault();
		if (receivedChildrenFromBack.length === children.length) {
			dispatch( putChildrenAsync( children ) );
		} else {
			const number = children.length - receivedChildrenFromBack.length;
			const body = children.slice( children.length - number );
			dispatch( postChildrenAsync( body ) );
		}
	};

	return (
		<>
			<form onSubmit={ submitFormAboutMe }>
				<TitleBigBlue whatClass={ 'myProfile-title' } text={ getTranslatedText( 'ownInfo.aboutMe', lang ) }/>
				<InputProfile
					label={ getTranslatedText( 'ownInfo.firstName', lang ) }
					type={ 'text' }
					name={ 'firstName' }
					value={ firstName }
					change={ changeInput }
				/>
				<InputProfile
					label={ getTranslatedText( 'ownInfo.lastName', lang ) }
					type={ 'text' }
					name={ 'lastName' }
					value={ lastName }
					change={ changeInput }
				/>
				<div>
					{ phonesArray.map( ( phone, idx ) => {
						return (
							<div key={ `${ idx }_phone` }>
								<InputProfile
									id={ idx }
									label={ `${ !idx ? getTranslatedText( 'ownInfo.phone', lang ) : '' } ` }
									type={ 'phone' }
									name={ 'phoneNumber' }
									value={ phone.phoneNumber }
									change={ ( e ) => changeInputPhone( e, idx ) }
								/>
							</div>
						);
					} ) }
				</div>
				<BlockButtons
					mb={ '34px' }
					index={ phonesArray.length - 1 }
					add={ phoneAdd }
					remove={ phoneRemove }
				/>
				<Button
					text={ getTranslatedText( 'button.saveChanges', lang ) }
					width={ '248px' }
					whatClass={ 'btn-form-about-me' }
					disabling={ prohibitionToSendAboutMe }
				/>
			</form>

			<form onSubmit={ submitFormChildren }>
				<div className={ 'block-children' }>
					<TitleBigBlue whatClass={ 'myProfile-title' } text={ getTranslatedText( 'ownInfo.children', lang ) }/>
					{ childrenArray.map( ( child, idx ) => {
						return (
							<div className={ 'block-child' } key={ `${ idx }_child` }>

								<InputProfile
									id={ idx }
									label={ getTranslatedText( 'ownInfo.dateOfBirth', lang ) }
									type={ 'date' }
									name={ 'birthDate' }
									value={ child.birthDate }
									change={ ( e ) => changeInputChildren( e, idx ) }/>
								<InputGender
									gender={ child.sex }
									id={ idx }
									click={ pickBoyOrGirl }/>
								<BlockButtons
									mb={ '37px' }
									index={ idx }
									id={ child.id }
									add={ childAdd }
									remove={ childDelete }
								/>
							</div>
						);
					} ) }
				</div>
				<Button
					disabling={ prohibitionToSendChildren }
					text={ getTranslatedText( 'button.saveChanges', lang ) }
					width={ '248px' }
					whatClass={ 'btn-form-children' }
				/>
			</form>
		</>
	);
};

export default MyProfile;
