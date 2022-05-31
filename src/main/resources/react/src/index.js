import { createRoot } from 'react-dom/client';

import App from 'app/App';

import './index.scss';
import './fonts/fontIcon.scss';

const root = createRoot(document.getElementById('root'));

root.render(<App />);
