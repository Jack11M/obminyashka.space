import { StyledError } from './styles';

const ErrorDisplay = ({ error }) => (
  <> {error && <StyledError>{error}</StyledError>}</>
);
export { ErrorDisplay };
