import { Icon } from 'obminyashka-components';

import { InboxDiv, InboxSpan } from './styles';

const InboxMessage = ({ inboxMessage }) => (
  <InboxDiv>
    <Icon.InboxMessage />
    <InboxSpan>{inboxMessage}</InboxSpan>
  </InboxDiv>
);

export { InboxMessage };
