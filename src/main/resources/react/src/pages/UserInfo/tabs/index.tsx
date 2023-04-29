import { getTranslatedText } from 'src/components/local/localization';

import { links } from './config';
import * as Styles from './styles';

const Tabs = ({ toggle }) => (
  <Styles.TabsBlock>
    {links.map(({ url, onClick, end, icon, textKey }) => (
      <Styles.NavLink to={url} end={end} key={url} onClick={onClick ? toggle : undefined}>
        {icon}
        <span>{getTranslatedText(textKey)}</span>
        <Styles.Circle />
      </Styles.NavLink>
    ))}
  </Styles.TabsBlock>
);

export { Tabs };
