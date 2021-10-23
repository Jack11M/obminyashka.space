import styled, { css } from 'styled-components';

const LabelSquare = styled.div`
  position: relative;
  margin-right: 20px;
  box-sizing: border-box;
  width: 17px;
  height: 17px;
  vertical-align: bottom;
  cursor: pointer;
  transition: all ease-in-out 0.3s;
  ${({ theme: { colors } }) => css`
    border: 3px solid ${colors.colorGrey};
  `}

  & > svg {
    position: absolute;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
  }
`;

const Input = styled.input`
  visibility: hidden;
  width: 0;
  height: 0;
  ${({ theme: { colors } }) => css`
    &:checked ~ ${LabelSquare} {
      border: 3px solid ${colors['btn-blue-normal']};
      background-color: ${colors['btn-blue-normal']};
    }

    &:checked ~ span {
      color: ${colors['black-color-text']};
    }
  `}
`;

const Div = styled.div`
  display: flex;
  transition: all ease-in-out 0.3s;

  ${(p) => css`
    margin: ${p.margin || '0 0 22px 0'};
  `}
`;

const Label = styled.label`
  display: flex;
  font-size: ${({ fontSize }) => (fontSize ? fontSize : '14px')};
  line-height: 17px;
  vertical-align: middle;
  cursor: pointer;
  max-width: 679px;
  width: 100%;
  & > span {
    line-height: 20px;
  }

  ${(p) => css`
    color: ${p.theme.colors.colorTextDisabled}};
    &:hover {
     ${Input}:not(input:checked) ~ ${LabelSquare} {
        & > svg {
          & > path {
            fill: ${p.theme.colors.colorGrey};
          }
        }
      }

  `}
`;

export { Div, Label, LabelSquare, Input };
