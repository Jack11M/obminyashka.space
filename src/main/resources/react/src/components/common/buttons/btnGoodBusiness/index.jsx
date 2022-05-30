import { Link } from 'react-router-dom';
import styled from 'styled-components';

import { ReactComponent as HeartSvg } from 'assets/icons/heart.svg';

const StyledLink = styled(Link)`
  > span {
    padding-top: 5px;
  }
`;

const BtnGoodBusiness = ({ href, whatClass, text }) => (
  <StyledLink to={href} className={whatClass}>
    <span>
      <HeartSvg />
    </span>

    {text}
  </StyledLink>
);

export { BtnGoodBusiness };
