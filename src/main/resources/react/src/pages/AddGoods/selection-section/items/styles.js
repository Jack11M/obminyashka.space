import styled, { css } from 'styled-components';
import { animated } from 'react-spring';

const WrapSelect = styled.div`
  position: relative;
  max-width: 350px;
  height: 50px;
`;
const SelectLabel = styled.label`
  display: flex;
  align-items: center;
  border: 1px solid #bcbcbc;
  border-radius: 2px;
  cursor: pointer;
  padding-left: ${(p) => (!p.showImg ? '6px' : 0)};
`;
const AnimatedLabel = styled(animated.div)`
  display: flex;
  align-items: center;
`;
const PlaceHolder = styled.p`
  padding: 10px;
  line-height: 26px;
  color: #8e8e8e;
`;
const DropItems = styled.div`
  position: absolute;
  max-width: 350px;
  width: 100%;
  border: ${(p) => p.notOpen? 'none': '1px solid #bcbcbc'};
  border-top-width: 0;
  background-color: #fff;
  overflow: hidden;
  z-index: 2;
  & > div:last-child > div {
    border-bottom: none;
  }
`;
const SelectItem = styled.div`
  display: flex;
  align-items: center;
  border-bottom: 1px solid #bcbcbc;
  transition: ease-in-out 0.3s;
  cursor: pointer;
  padding-left: ${(p) => (!p.showImg ? '8px' : 0)};
  ${(p) =>
    p.selectedItem &&
    css`
      background-color: hsl(195, 100%, 80%);
    `}
  &:hover {
    background-color: hsl(195, 100%, 90%);
  }
`;

const Image = styled.img`
  width: 18px;
  height: 18px;
  margin-left: 15px;
`;

const SelectTitle = styled.p`
  padding: 10px;
  line-height: 26px;
  color: #11171f;
`;

export {
  Image,
  DropItems,
  SelectItem,
  WrapSelect,
  PlaceHolder,
  SelectLabel,
  SelectTitle,
  AnimatedLabel,
};
