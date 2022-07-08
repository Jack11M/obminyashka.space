import { H2 } from './styles';

const TitleBigBlue = ({ whatClass = '', text, style }) => (
  <H2 className={whatClass} style={style}>
    {text}
  </H2>
);

export default TitleBigBlue;
