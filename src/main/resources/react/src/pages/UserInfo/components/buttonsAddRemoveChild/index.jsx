import * as Styles from './styles';

const ButtonsAddRemoveChild = ({ add, text, onClick }) => (
  <Styles.AddRemoveItem add={add}>
    {text && <span>{text}</span>}
    <Styles.Span add={add} onClick={onClick} />
  </Styles.AddRemoveItem>
);

export default ButtonsAddRemoveChild;
