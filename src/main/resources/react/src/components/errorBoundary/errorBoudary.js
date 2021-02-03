import React from "react";
import { withRouter } from "react-router-dom";
import SomethingBad from '../../pages/ErrorPages/SomethingBad';

class ErrorBoundary extends React.Component {
  state = { hasError: false };

  componentDidCatch(error, info) {
    console.log(error);
    console.log(info);
    this.setState({ hasError: true });
  }


  deactivateError = (value) => {
    this.setState({ hasError: value });
  };

  render() {
    if (this.state.hasError) {
      return <SomethingBad deactivateError={this.deactivateError} />;
    }
    return this.props.children;
  }
}

export default withRouter(ErrorBoundary);
