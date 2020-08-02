import React from "react";
import { connect } from "react-redux";
import { v4 as uuidv4 } from 'uuid';

import Title_Big_Blue from "../../../../components/title_Big_Blue";
import InputData from "../../components/inputData/inputData";
import BlockButtons from "../../components/blockButtons";
import InputChildrenList from "../../components/inputChildrenList";

import "./myProfile.scss";

const MyProfile = ({ state }) => {
  const { me, children } = state;

  const addInput = (e) => {
  console.log(e.target)
  }

  return (
    <form>
      <Title_Big_Blue whatClass={"myProfile-title"} text={"О себе"} />
      {me.map((input) => {
        return <InputData data={input} key={uuidv4()} addInput={addInput}/>;
      })}
      <div className={'block-children'}>
        <Title_Big_Blue whatClass={"myProfile-title"} text={"Дети"} />
        {children.map((item, idx) => (
          <div className={'block-child'} key={uuidv4()}>
            <InputChildrenList data={item}  />
            <BlockButtons  index={idx}/>
          </div>
        ))}
        </div>
    </form>
  );
};
const mapStateToProps = (state) => {
  console.log(state)
  return {
    state: state.stateProfile,
  };
};

export default connect(mapStateToProps)(MyProfile);
