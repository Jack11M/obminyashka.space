import styled, { css } from 'styled-components';

const LabelSquare = styled.div`
  position: relative;
  -webkit-box-sizing: border-box;
  box-sizing: border-box;
  width: 17px;
  height: 17px;
  vertical-align: bottom;
  cursor: pointer;
  transition: all ease-in-out 0.3s;

  ${({ theme: { colors }, checked }) => css`
    border: 3px solid ${checked ? colors.btnBlue : colors.colorGrey};
    background-color: ${checked && colors.btnBlue};
  `}

  & > svg {
    position: absolute;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
  }

  &:hover {
  }
`;

const Label = styled.label`
  display: block;
  margin-left: ${(props) => (props.distanceBetween ? '9px' : '22px')};
  font-size: ${({ fontSize }) => fontSize || '14px'};
  line-height: 17px;
  vertical-align: middle;
  cursor: pointer;
  max-width: 679px;
  width: 100%;
  color: ${({ theme: { colors }, checked }) =>
    checked ? colors.blackColorText : colors.colorTextDisabled};
`;

const Div = styled.div`
  display: flex;
  transition: all ease-in-out 0.3s;

  ${(p) => css`
    margin: ${p.margin || '22px'};

    &:hover {
      ${LabelSquare} {
        & > svg {
          & > path {
            fill: ${p.checked ? 'white' : p.theme.colors.colorGrey};
          }
        }
      }
    }
  `}
`;

export { Div, Label, LabelSquare };
