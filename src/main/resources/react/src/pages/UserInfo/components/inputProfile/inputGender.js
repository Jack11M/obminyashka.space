import React from 'react';
import styled from 'styled-components';

const GenderDiv = styled.div`
 	display: flex;
  justify-content: space-between;
  align-items: center;
  margin: 3px 0 26px;
`;
const Container = styled.div`
	display: flex;
	width: 415px;
`;

const Label = styled.label`
  font-size: 14px;
  line-height: 16px;
  display: inline-flex;
  
`;
const ChildDiv = styled.div`
 cursor: pointer;
 margin-right: 41px;
  
`;
const CircleBoy = styled.span`
	display: inline-flex;
	width: 15px;
	height: 15px;
	border: 4px solid ${ ( { theme: { colors } } ) => colors['btn-blue-normal'] };
	border-radius: 50%;
	background-color: ${ ( { theme: { colors }, gender } ) => {
	return gender === 'boy' ? colors['btn-blue-normal'] : colors['bg-content'];
} };
	& + span {
	color: ${ ( { theme: { colors }, gender } ) => {
	return gender === 'boy' && colors['black-color-text'];
} };
	}
`;
const CircleGirl = styled.span`
	display: inline-flex;
	width: 15px;
	height: 15px;
	border: 4px solid ${ ( { theme: { colors } } ) => colors['btn-blue-normal'] };
	border-radius: 50%;
	background-color: ${ ( { theme: { colors }, gender } ) => {
	return gender === 'girl' ? colors['btn-blue-normal'] : colors['bg-content'];
} };
	& + span {
	color: ${ ( { theme: { colors }, gender } ) => {
	return gender === 'girl' && colors['black-color-text'];
} };
	}
`;
const Span = styled.span`
  display: inline-flex;
  margin-left: 9px;
	font-size: 16px;
  line-height: 20px;
  vertical-align: bottom;
  color: ${ ( { theme: { colors } } ) => colors.colorGrey };

`;

const InputGender = ( props ) => {
	const { gender, id } = props.data;


	return (
		<GenderDiv>
			<Label>{ 'Пол:' }</Label>
			<Container>
				<ChildDiv onClick={ () => props.click( id, 'boy' ) }>
					<CircleBoy gender={ gender }/>
					<Span>Мальчик</Span>
				</ChildDiv>
				<ChildDiv onClick={ () => props.click( id, 'girl' ) }>
					<CircleGirl gender={ gender }/>
					<Span>Девочка</Span>
				</ChildDiv>
			</Container>
		</GenderDiv>
	);
};

export default InputGender;
