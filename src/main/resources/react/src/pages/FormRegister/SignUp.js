import React from 'react';
import styled from 'styled-components';

import { getTranslatedText } from '../../components/local/localisation';
import CheckBox from '../../components/checkbox';
import Button from '../../components/button/Button';
import InputForAuth from './InputForAuth';

// const Div = styled.div`
//
// `
//
// const InputDiv = styled.div`
//   margin-bottom: 22px;
//
//   &:last-child {
//     margin-bottom: 32px;
//   }
// `;
//
// const Label = styled.label`
//   font-style: normal;
//   font-weight: normal;
//   font-size: 14px;
//   line-height: 22px;
//   color: ${ ( { theme: { colors } } ) => colors.colorGrey };
//   cursor: pointer;
// `;
//
// const InputAuth = styled.input`
//   padding: 12px 16px;
//   width: 100%;
//   border: 1px solid hsl(0, 0%, 74%);;
//   border-radius: 2px;
//   box-sizing: border-box;
//   font-family: Roboto, sans-serif;
//   font-style: normal;
//   font-weight: normal;
//   font-size: 16px;
//   line-height: 24px;
//   outline: none;
//   color: ${ ( { theme: { colors } } ) => colors['right-color-text'] };
//
//   &:focus {
//     border-color: hsl(0, 0%, 44%);
//   }
// `;

const Extra = styled.div`
  display: flex;
  justify-content: space-between;
`;

const Check = styled( CheckBox )`
  ${ ( CheckBox ) => console.log( CheckBox ) }

`;
const SignUp = () => {
	return (
		<form>
			<div>
				<InputForAuth
					text={ getTranslatedText( 'auth.regEmail' ) }
					name={ 'regEmail' }
					type={ 'text' }
					error={ '' }
				/>
				<InputForAuth
					text={ getTranslatedText( 'auth.regLogin' ) }
					name={ 'regLogin' }
					type={ 'text' }
					error={ '' }
				/>
				<InputForAuth
					text={ getTranslatedText( 'auth.regPassword' ) }
					name={ 'regPassword' }
					type={ 'password' }
					error={ '' }
				/>
				<InputForAuth
					text={ getTranslatedText( 'auth.regConfirm' ) }
					name={ 'regConfirm' }
					type={ 'password' }
					error={ '' }
				/>
			</div>
			<Extra>
				<Check
					text={ getTranslatedText('auth.agreement') }
					margin={ '0 0 44px 0' }
					fs={ '14px' }
					checked={ true }
				/>
			</Extra>
			<Button
				text={ getTranslatedText( 'auth.signUp' ) }
				disabling={ false }
				mb={ '44px' }
				bold
				lHeight={ '24px' }
				width={ '222px' }
				height={ '48px' }/>
		</form>
	);
};

export default SignUp;