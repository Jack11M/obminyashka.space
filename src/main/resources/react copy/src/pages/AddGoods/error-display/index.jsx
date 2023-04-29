import { StyledError } from './styles';

const ErrorDisplay = ({ error, marginTop }) => (
  <> {error && <StyledError marginTop={marginTop}>{error}</StyledError>}</>
);
export { ErrorDisplay };
