import * as Styles from './styles';

const ButtonsAddRemoveChild = ({ add, text, onClick }) => (
  <Styles.AddRemoveItem add={add} onClick={onClick}>
    {text && <span>{text}</span>}
    <Styles.Span add={add} />
  </Styles.AddRemoveItem>
);

export default ButtonsAddRemoveChild;
