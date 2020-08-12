import React from "react";
import { connect } from "react-redux";
import { v4 as uuidv4 } from "uuid";

import TitleBigBlue from "../../../../components/title_Big_Blue";
import InputData from "../../components/inputData/inputData";
import BlockButtons from "../../components/blockButtons";

import "./myProfile.scss";
import "./buttonForProfile.scss";
import Button from "../../../../components/button/Button";

const MyProfile = ({ state }) => {
  const { me, children } = state;

  const addInput = (e) => {
    // console.log(e.target);
  };

  return (
    <form>
      <TitleBigBlue whatClass={"myProfile-title"} text={"О себе"} />
      {me.map((input) => {
        return <InputData data={input} key={uuidv4()} addInput={addInput} />;
      })}
      <div className={"block-children"}>
        <TitleBigBlue whatClass={"myProfile-title"} text={"Дети"} />
        {children.map((item, idx) => {
          return (
            <div className={"block-child"} key={uuidv4()}>
              <InputData data={item.childName} />
              <InputData data={item.childAge} />
              <BlockButtons index={idx} />
            </div>
          );
        })}
      </div>
      <Button text={"Сохранить"} whatClass={"btn-profile"} />
    </form>
  );
};
const mapStateToProps = (state) => {
  return {
    state: state.stateProfile,
  };
};

export default connect(mapStateToProps)(MyProfile);
