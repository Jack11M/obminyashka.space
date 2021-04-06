import React, { useEffect, useRef, useState } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import styled from 'styled-components';
import { chooseLanguage, clearValueLogin, clearValueSignUp } from '../../redux/auth/action';
import { changeLangProfileErrors } from '../../redux/profile/profileAction';

const SelectLanguage = styled.div`
  width: 29px;
  margin: 22px 15px 0;
`;

const LanguageLabel = styled.div`
  position: relative;
  font-family: inherit;
  font-size: 14px;
  font-style: normal;
  font-weight: 400;
  line-height: 19px;
  color: #777777;
  text-transform: uppercase;
  cursor: pointer;

  &:after {
    position: absolute;
    content: "";
    display: block;
    top: 38%;
    right: 0;
    border-style: solid;
    border-width: 5px 2.5px 0 2.5px;
    border-color: #777777 transparent transparent transparent;
    z-index: 1;
    pointer-events: none;

  }
`;

const DropDownMenu = styled.div`
  position: absolute;
  background-color: #F2F4F7;
  color: #777777;
  box-sizing: border-box;
  cursor: pointer;
  z-index: 1000;

`;

const DivOption = styled.div`
  text-transform: uppercase;
  font-family: inherit;
  font-size: 12px;
  font-style: normal;
  font-weight: 400;
  line-height: 19px;
  padding: 9px;
  background-color: ${ props => props.checked ? 'hsl(195, 100%, 53%)' : '#F2F4F7' };
  color: ${ props => props.checked ? '#fff' : '#777777' };

  &:hover {
    background-color: hsl(195, 100%, 90%);
    color: hsl(0, 0%, 10%);;
  }
`;

const languageArray = [
	{ value: 'ru', checked: false },
	{ value: 'ua', checked: false },
	{ value: 'en', checked: false }
];

const CustomSelect = () => {
	const dispatch = useDispatch();
	const { lang: language } = useSelector( state => state.auth );
	const catchRef = useRef();

	const selectedOption = languageArray.map( lang => lang.value === language ? { ...lang, checked: true } : { ...lang, checked: false } );

	const pickLanguage = selectedOption.find( option => option.checked );
	const [ open, setOpen ] = useState( false );


	const handleCatch = ( e ) => {
		const path = e.path || (e.composedPath && e.composedPath());
		if (!path.includes( catchRef.current )) {
			setOpen( false );
		}
	};

	useEffect( () => {
		document.body.addEventListener( 'click', handleCatch );
	}, [] );

	const handleOpen = () => {
		setOpen( prevOpen => !prevOpen );
	};

	const handleSelected = ( lang ) => {
		dispatch( clearValueSignUp() );
		dispatch( clearValueLogin() );
		dispatch(changeLangProfileErrors(lang))
		dispatch( chooseLanguage( lang ) );
	};

	return (
		<SelectLanguage ref={ catchRef } onClick={ handleOpen }>
			<LanguageLabel>{ pickLanguage.value }</LanguageLabel>
			{ open && <DropDownMenu>
				{ selectedOption.map( option => <DivOption key={ option.value } onClick={ () => handleSelected( option.value ) }
					checked={ option.checked }>{ option.value }</DivOption> ) }
			</DropDownMenu> }
		</SelectLanguage>
	);
};

export default CustomSelect;
