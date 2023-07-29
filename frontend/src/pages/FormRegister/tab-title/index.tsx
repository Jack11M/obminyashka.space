import React, { useCallback } from "react";

import * as Styles from "./style";

type Props = {
  title: string;
  index: number;
  selectedTab: number;
  setVariant: (index: number) => void;
  setSelectedTab: (index: number) => void;
};

const TabTitle: React.FC<Props> = ({
  title,
  index,
  setVariant,
  selectedTab,
  setSelectedTab,
}) => {
  const onClick = useCallback(() => {
    setSelectedTab(index);
  }, [setSelectedTab, index]);

  return (
    <Styles.Title
      onClick={() => setVariant(index)}
      className={`${selectedTab === index ? "active" : ""}`}
    >
      <Styles.Button onClick={onClick}>{title}</Styles.Button>
    </Styles.Title>
  );
};

export default TabTitle;
