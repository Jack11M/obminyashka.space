import { Link } from 'react-router-dom';

import { Icon } from '@wolshebnik/obminyashka-components';

import styled from 'styled-components';

const StyledLink = styled(Link)`
  > span {
    padding-top: 5px;
  }
`;

const BtnGoodBusiness = ({ href, whatClass, text }) => (
  <StyledLink to={href} className={whatClass}>
    <span>
      <Icon.Heart />
    </span>

    {text}
  </StyledLink>
);

export { BtnGoodBusiness };
