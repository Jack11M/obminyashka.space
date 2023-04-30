/* eslint-disable */
// @ts-nocheck
// TODO: fix typescript
import React from 'react';

export function withRef(Component) {
  return React.forwardRef((props, ref) => <Component ref={ref} {...props} />);
}
