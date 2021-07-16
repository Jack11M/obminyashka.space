import React from 'react';
import { Link } from 'react-router-dom';
import styled from 'styled-components';

import { ReactComponent as HeartSvg } from 'assets/icons/heart.svg';

const StyledLink = styled(Link)`
  > span {
    padding-top: 5px;
  }
`;

const BtnGoodBusiness = (props) => {
  return (
    <StyledLink to={props.href} className={props.whatClass}>
      <span>
        <HeartSvg />
      </span>
      {props.text}
    </StyledLink>
  );
};

export default BtnGoodBusiness;
