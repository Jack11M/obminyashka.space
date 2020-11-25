import React from 'react';
import styled from 'styled-components';


const Div = styled.div`
	margin: ${ props => props.margin };
`;

const LabelSquare = styled.span`
	position: relative;
	display: inline-flex;
  box-sizing: border-box;
  width: 17px;
  height: 17px;
  vertical-align: bottom;
  cursor: pointer;
  border: 3px solid ${ ( { theme: { colors }, checked } ) => {
	return checked ? colors['btn-blue-normal'] : colors.colorGrey;
} };;
  background-color: ${ ( { theme: { colors }, checked } ) => {
	return checked && colors['btn-blue-normal'];
} };
`;

const Svg = styled.svg`
	position: absolute;
	top: 50%;
	left: 50%;
	transform: translate(-50%, -50%);
`;

const Label = styled.span`
	display: inline-flex;
 	margin-left: ${ props => props.distanceBetween ? '9px' : '22px' };
  cursor: pointer;
  color: ${ ( { theme: { colors }, checked } ) => {
	return checked ? colors['black-color-text'] : colors.colorGrey ;
} };;
`;

const CheckBox = ( props ) => {
	return (
		<Div margin={ props.margin } onClick={ props.click}>
			<LabelSquare checked={props.checked}>
				{ props.checked && (<Svg width="10" height="8" viewBox="0 0 10 8" fill="none" xmlns="http://www.w3.org/2000/svg">
					<mask id="path-1-inside-1" fill="white">
						<path fillRule="evenodd" clipRule="evenodd" d="M10.0007 0.835938L4.10237 7.00116L0.998047 3.75638"/>
					</mask>
					<path fillRule="evenodd" clipRule="evenodd" d="M10.0007 0.835938L4.10237 7.00116L0.998047 3.75638" fill="black" fillOpacity="0.01"/>
					<path
						d="M4.10237 7.00116L2.65722 8.38375L4.10238 9.89429L5.54752 8.38375L4.10237 7.00116ZM8.55551 -0.546645L2.65722 5.61858L5.54752 8.38375L11.4458 2.21852L8.55551 -0.546645ZM5.54751 5.61857L2.44319 2.37379L-0.447099 5.13897L2.65722 8.38375L5.54751 5.61857Z" fill="white" mask="url(#path-1-inside-1)"/>
				</Svg>) }

			</LabelSquare>
			<Label checked={props.checked} distanceBetween={ props.distanceBetween }>Разрешаю отображать на сайте</Label>
		</Div>
	);
};

export default CheckBox;
