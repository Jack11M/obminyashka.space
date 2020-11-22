import React from 'react';
import styled from 'styled-components';

const Div = styled.div`
	margin: ${props => props.margin};
	

`
const InputNone = styled.input`
	display: none;
`
const LabelSquare = styled.span`
	position: relative;
	display: inline-flex;
  box-sizing: border-box;
  width: 17px;
  height: 17px;
  vertical-align: bottom;
  cursor: pointer;
  border: 3px solid #8E8E8E;
`

const Label = styled.span`
	display: inline-flex;
 	margin-left: 9px;
  cursor: pointer;
  color: #8E8E8E;
`

const CheckBox = (props) => {
	return (
		<Div margin={props.margin}>
			<InputNone type={'checkbox'}/>
			<LabelSquare/>
			<Label>Разрешаю отображать на сайте</Label>
		</Div>
	);
};

export default CheckBox;
