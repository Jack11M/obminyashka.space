import React from "react";
import ErrorPage from "../../pages/ErrorPages/ErrorPage";
import { withRouter } from "react-router-dom";

class ErrorBoundary extends React.Component {
  state = { hasError: false };

  componentDidCatch(error, info) {
    console.log(error);
    console.log(info);
    this.setState({ hasError: true });
  }

  goTo = (event) => {
    this.setState({ hasError: false });
    if (event.target.className === "onMain") {
      this.props.history.push("/");
    } else {
      this.props.history.push(this.props.location.pathname);
    }
  };

  render() {
    if (this.state.hasError) {
      return <ErrorPage goTo={this.goTo} />;
    }
    return this.props.children;
  }
}

export default withRouter(ErrorBoundary);
