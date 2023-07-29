import React, { ReactElement, useState } from "react";

import TabTitle from "../tab-title";
import * as Styles from "./styles";

type Props = {
  children: ReactElement[];
  setVariant: (index: number) => void;
};

const Tabs: React.FC<Props> = ({ children, setVariant }) => {
  const [selectedTab, setSelectedTab] = useState(0);

  return (
    <>
      <Styles.TabWarper>
        {children.map((item, index) => (
          <TabTitle
            key={index}
            index={index}
            setVariant={setVariant}
            title={item.props.title}
            selectedTab={selectedTab}
            setSelectedTab={setSelectedTab}
          />
        ))}
      </Styles.TabWarper>
      {children[selectedTab]}
    </>
  );
};

export default Tabs;
