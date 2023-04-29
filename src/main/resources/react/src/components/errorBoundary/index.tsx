/* eslint-disable */
// @ts-nocheck
import React from 'react';
import { showMessage } from 'obminyashka-components';

import { withRouter } from 'src/hooks/withRouter';

import SomethingBad from '../../pages/ErrorPages/SomethingBad';

class ErrorBoundary extends React.Component {
  state = { hasError: localStorage.getItem('error') };

  componentDidCatch(error, info) {
    showMessage.error(error);
    showMessage.info(info);
    this.setState({ hasError: true });
  }

  deactivateError = (value) => {
    this.setState({ hasError: value });
  };

  render() {
    if (this.state.hasError) {
      return <SomethingBad deactivateError={this.deactivateError} />;
    }
    return this.props?.children;
  }
}

export default withRouter(ErrorBoundary);
