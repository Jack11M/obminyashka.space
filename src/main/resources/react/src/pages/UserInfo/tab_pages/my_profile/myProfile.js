import React, { useCallback } from 'react';
import { useDispatch, useSelector } from 'react-redux';

import TitleBigBlue from '../../../../components/title_Big_Blue';
import InputProfile from '../../components/inputProfile/inputProfile';
import BlockButtons from '../../components/buttonsAddRemoveChild/blockButtons';
import Button from '../../../../components/button/Button';
import InputGender from '../../components/inputProfile/inputGender';
import CheckBox from '../../../../components/checkbox';

import './myProfile.scss';

import {
	addChild,
	addChildrenInputValue,
	addMeInputValue, addPhone,
	addPhoneInputValue,
	deleteChild, deletePhone,
	putUserInfoAsync
} from '../../../../redux/profile/profileAction';


const MyProfile = () => {
	const dispatch = useDispatch();
	const { profile } = useSelector( state => state.profileMe );

	const addInputMe = useCallback( ( e ) => {
		dispatch( addMeInputValue( { [e.target.name]: e.target.value } ) );
	}, [ dispatch ] );

	const addInputCity = ( e ) => {
		console.log( { [e.target.name]: e.target.value } );
	};

	const addInputPhone = useCallback( ( e, id ) => {
		dispatch( addPhoneInputValue( { [e.target.name]: e.target.value }, id ) );
	}, [ dispatch ] );

	const phoneAdd = useCallback(()=> {
		dispatch(addPhone())
	},[dispatch])

	const phoneRemove = useCallback(()=> {
		dispatch(deletePhone())
	},[dispatch])

	const showPhone = useCallback( ( value, id ) => {
		dispatch( addPhoneInputValue( { show: !value }, id ) );
	}, [ dispatch ] );

	const addInputChildren = useCallback( ( e, id ) => {
		dispatch( addChildrenInputValue( { [e.target.name]: e.target.value }, id ) );
	}, [ dispatch ] );

	const pickBoyOrGirl = useCallback( ( id, gender ) => {
		dispatch( addChildrenInputValue( { sex: gender }, id ) );
	}, [ dispatch ] );

	const childDelete = useCallback( ( childrenId ) => {
		dispatch( deleteChild( childrenId ) );
	}, [ dispatch ] );

	const childAdd = useCallback( id => {
		dispatch( addChild() );
	}, [ dispatch ] );

	const submitFormAboutMe = ( e ) => {
		e.preventDefault();
		dispatch( putUserInfoAsync( profile ) );
	};

	const submitFormChildren = ( e ) => {
		e.preventDefault();
		dispatch( putUserInfoAsync( profile ) );
	};

	return (
		<>
			<form onSubmit={ submitFormAboutMe }>
				<TitleBigBlue whatClass={ 'myProfile-title' } text={ 'О себе' }/>
				<InputProfile
					data={ {
						name: 'firstName',
						label: 'Имя:',
						value: profile.firstName,
						type: 'text'
					} }
					click={ addInputMe }
				/>
				<InputProfile
					data={ {
						name: 'lastName',
						label: 'Фамилия:',
						value: profile.lastName,
						type: 'text'
					} }
					click={ addInputMe }
				/>
				<InputProfile data={ {
					name: 'city',
					label: 'Город:',
					value: profile.city,
					type: 'text'
				} }
					click={ addInputCity }
				/>

				<div>
					{ profile.phones.map( ( phone, index ) => {
						return (
							<div key={ `${ phone.id }_phone` }>
								<InputProfile
									data={ {
										name: 'phoneNumber',
										label: `${ !index ? 'Телефон:' : '' } `,
										value: phone.phoneNumber,
										type: 'phone',
										id: phone.id
									} }
									click={ ( e ) => addInputPhone( e, phone.id ) }
								/>
								<CheckBox
									margin={ '-10px 0 18px 134px' }
									checked={ phone.show }
									click={ () => showPhone( phone.show, phone.id ) }
									distanceBetween
								/>
							</div>
						);
					} ) }
				</div>
				<BlockButtons
					mb={ '34px' }
					index={ profile.phones.length - 1 }
					add={ phoneAdd }
					remove={ phoneRemove}
				/>


				<Button
					text={ 'Сохранить' }
					width={ '248px' }
					whatClass={ 'btn-form-about-me' }/>
			</form>

			<form onSubmit={ submitFormChildren }>
				<div className={ 'block-children' }>
					<TitleBigBlue whatClass={ 'myProfile-title' } text={ 'Дети' }/>
					{ profile.children.map( ( child, idx ) => {
						return (
							<div className={ 'block-child' } key={ `${ child.id }_child` }>
								<InputProfile
									data={ {
										name: 'name',
										label: 'Имя:',
										value: child.name,
										type: 'text'
									} }
									click={ ( e ) => addInputChildren( e, child.id ) }/>
								<InputProfile
									data={ {
										name: 'birthDate',
										label: 'Возраст:',
										value: child.birthDate,
										type: 'date'
									} }
									click={ ( e ) => addInputChildren( e, child.id ) }/>
								<InputGender
									data={ {
										gender: child.sex,
										id: child.id
									} }
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
					text={ 'Сохранить' }
					width={ '248px' }
					whatClass={ 'btn-form-children' }
				/>
			</form>
		</>
	);
};

export default MyProfile;
