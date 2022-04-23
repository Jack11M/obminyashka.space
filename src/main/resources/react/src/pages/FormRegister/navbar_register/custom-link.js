import { useMatch, useResolvedPath } from 'react-router-dom';
import { StyledLink } from './styles';

const CustomLink = ({ children, to, ...props }) => {
  let resolved = useResolvedPath(to);
  const match = useMatch({ path: resolved.pathname });

  return (
    <StyledLink to={to} className={!!match ? 'active-link' : ''} {...props}>
      {children}
    </StyledLink>
  );
};

export { CustomLink };
