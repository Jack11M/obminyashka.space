import React from 'react';
import styled from 'styled-components';

const FavoriteSvgStar = styled.svg`
	display: inline-block;
	width: 100%;
	margin: 11px auto;

	path {
	fill: ${ ({ isFavorite })=> isFavorite ?'#12b6ed': '#fff'};
	}
`

const SvgStar = (props) => {
	return (
		<FavoriteSvgStar
			width="28"
			height="26.6"
			viewBox="0 0 28 28"
			fill="none"
			xmlns="http://www.w3.org/2000/svg"
			isFavorite={props.isFavorite}
		>
			<path
				d="M14 22.0816L22.6525 27.2973L20.3631 17.4589L28 10.8432L17.931 9.97576L14 0.697327L10.069 9.97576L0 10.8432L7.63686 17.4589L5.34745 27.2973L14 22.0816Z"
			/>
		</FavoriteSvgStar>
	)
}

export default SvgStar;
