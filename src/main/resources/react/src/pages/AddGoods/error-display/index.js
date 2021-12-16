import { StyledError } from './styles';

const ErrorDisplay = ({ error }) => {
  return <>{error && <StyledError>{error}</StyledError>}</>;
};
export { ErrorDisplay };
