import { ToastContainer } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';

import * as Styles from './styles';

const Toast = (props) => (
  <Styles.Container>
    <ToastContainer {...props} />
  </Styles.Container>
);

export { Toast };
