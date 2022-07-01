import { H2 } from './styles';

const TitleBigBlue = ({ whatClass = '', text }) => (
  <H2 className={whatClass}>{text}</H2>
);

export default TitleBigBlue;
