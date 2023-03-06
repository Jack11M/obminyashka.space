import React from 'react';
import { showMessage } from 'obminyashka-components';

import { withRouter } from 'hooks/withRouter';

import SomethingBad from '../../pages/ErrorPages/SomethingBad';

class ErrorBoundary extends React.Component {
  // eslint-disable-next-line react/state-in-constructor
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
    // eslint-disable-next-line react/destructuring-assignment
    if (this.state.hasError) {
      return <SomethingBad deactivateError={this.deactivateError} />;
    }
    // eslint-disable-next-line react/destructuring-assignment
    return this.props.children;
  }
}

export default withRouter(ErrorBoundary);
