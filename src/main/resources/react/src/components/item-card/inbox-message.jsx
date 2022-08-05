import * as Icon from 'assets/icons';

import { InboxDiv, InboxSpan } from './styles';

const InboxMessage = ({ inboxMessage }) => (
  <InboxDiv>
    <Icon.InboxMessageSvg />
    <InboxSpan>{inboxMessage}</InboxSpan>
  </InboxDiv>
);
export { InboxMessage };
