import React, { useCallback } from 'react';
import { useDispatch, useSelector } from 'react-redux';

import TitleBigBlue from '../../../../components/title_Big_Blue';
import InputProfile from '../../components/inputProfile/inputProfile';
import BlockButtons from '../../components/blockButtons';
import Button from '../../../../components/button/Button';

import './myProfile.scss';
import './buttonForProfile.scss';

import { addChildrenInputValue, addMeInputValue, addPhoneInputValue, putUserInfoAsync } from '../../../../redux/profile/profileAction';


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

	const addInputChildren = useCallback( ( e, id ) => {
		dispatch( addChildrenInputValue( { [e.target.name]: e.target.value }, id ) );
	}, [ dispatch ] );

	const submitForm = ( e ) => {
		e.preventDefault();
		dispatch( putUserInfoAsync( profile ) );
	};

	return (
		<form onSubmit={ submitForm }>
			<TitleBigBlue whatClass={ 'myProfile-title' } text={ 'О себе' }/>
			<InputProfile
				data={ {
					name: 'firstName',
					label: 'Имя:',
					value: profile.firstName,
					type: 'text'
				} }
				click={ addInputMe }/>
			<InputProfile
				data={ {
					name: 'lastName',
					label: 'Фамилия:',
					value: profile.lastName,
					type: 'text'
				} }
				click={ addInputMe }/>
			<InputProfile data={ {
				name: 'city',
				label: 'Город:',
				value: profile.city,
				type: 'text'
			} }
				click={ addInputCity } disabled/>

			{ profile.phones.map( ( phone, index ) => {
				return <InputProfile
					data={ {
						name: 'phoneNumber',
						label: `${ !index ? 'Телефон:' : '' } `,
						value: phone.phoneNumber, type: 'phone'
					} }
					key={ index }
					click={ ( e ) => addInputPhone( e, phone.id ) }/>;
			} ) }

			<div className={ 'block-children' }>
				<TitleBigBlue whatClass={ 'myProfile-title' } text={ 'Дети' }/>
				{ profile.children.map( ( child, idx ) => {
					return (
						<div className={ 'block-child' } key={ `${ child.id }_child` }>
							<InputProfile
								data={ {
									name: 'sex',
									label: 'Имя:',
									value: child.sex,
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
							<BlockButtons index={ idx } childrenId={ child.id }/>
						</div>
					);
				} ) }
			</div>
			<Button text={ 'Сохранить' } whatClass={ 'btn-profile' }/>

		</form>
	);
};

export default MyProfile;
