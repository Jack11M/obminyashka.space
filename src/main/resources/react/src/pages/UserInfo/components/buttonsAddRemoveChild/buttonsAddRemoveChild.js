import React from 'react';
import styled from 'styled-components';

const AddRemoveItem = styled.div`
  display: inline-flex;
  align-items: center;

  span:first-child {
    font-size: 14px;
    line-height: 16px;
    margin-right: 21px;
  }
  &.add-field {
    margin-left: 135px;
    margin-bottom: 34px;
  }
  &.remove-field {
    position: absolute;
    top: 3px;
    right: -50px;
  }
`;
const Span = styled.span`
  position: relative;
  display: inline-flex;
  width: 34px;
  height: 34px;
  color: #fff;
  border-radius: 50%;
  background-color: ${(props) =>
    props.addRemove ? 'hsl(134, 45%, 48%)' : 'hsl(0, 0%, 47%)'};
  transition: 0.2s;

  &:hover {
    cursor: pointer;
    background-color: ${(props) =>
      props.addRemove ? 'hsl(134, 45%, 43%)' : 'hsl(0, 0%, 42%)'};
  }

  :before {
    position: absolute;
    content: '';
    top: 16px;
    left: 12px;
    width: 10px;
    height: 2px;
    background-color: #fff;
    transform: ${(props) =>
      props.addRemove ? 'rotate(0deg)' : 'rotate(45deg)'};
  }

  :after {
    position: absolute;
    content: '';
    top: 12px;
    left: 16px;
    width: 2px;
    height: 10px;
    background-color: #fff;
    transform: ${(props) =>
      props.addRemove ? 'rotate(0deg)' : 'rotate(45deg)'};
  }
`;

const ButtonsAddRemoveChild = (props) => {
  const { text, addRemove, onClick } = props;
  return (
    <AddRemoveItem className={props.className}>
      <span>{text}</span>
      <Span addRemove={addRemove} onClick={onClick} />
    </AddRemoveItem>
  );
};

export default ButtonsAddRemoveChild;
