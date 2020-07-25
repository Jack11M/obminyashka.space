import React from "react";
import ReactDOM from "react-dom";
import App from "./App";
import { Provider } from "react-redux";
import { createStore } from "redux";

import "./index.scss";
import "./fonts/fontIcon.scss";

ReactDOM.render(<App />, document.getElementById("root"));
