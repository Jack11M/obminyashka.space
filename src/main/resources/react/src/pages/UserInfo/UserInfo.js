import React from "react";
import { useRouteMatch } from "react-router-dom";

import Tabs from "./tabs";
import RouterTabs from "./tab_pages/router_tabs";
import ActiveProfile from "./active_profile";

import "./UserInfo.scss";

const UserInfo = () => {
  let { path, url } = useRouteMatch();

  return (
    <div className="container">
      <aside className="left-side">
        <ActiveProfile />
        <Tabs url={url} />
      </aside>
      <main className="main-content">
        <div className="main-content-wrapper">
          <RouterTabs path={path} />
        </div>
      </main>
    </div>
  );
};

export default UserInfo;
