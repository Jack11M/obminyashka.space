import styled, { css } from 'styled-components';

const LabelSquare = styled.div`
  position: relative;
  margin-right: 20px;
  box-sizing: border-box;
  vertical-align: bottom;
  cursor: pointer;
  transition: all ease-in-out 0.3s;

  ${({ theme, type }) => css`
    width: ${type === 'checkbox' ? '17px' : '20px'};
    height: ${type === 'checkbox' ? '17px' : '20px'};
    border: 3px solid ${theme.colors.colorGrey};
    border-radius: ${type === 'checkbox' ? '1px' : '50%'};
  `}

  & > svg {
    position: absolute;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
    opacity: 0;
  }
`;

const Input = styled.input`
  visibility: hidden;
  width: 0;
  height: 0;

  ${({ theme: { colors } }) => css`
    &:checked ~ ${LabelSquare} {
      border: 3px solid ${colors.btnBlue};
      background-color: ${colors.btnBlue};

      svg {
        opacity: 1;
      }
    }

    &:checked ~ span {
      color: ${colors.blackColorText};
    }
  `}
`;

const Div = styled.div`
  display: flex;
  transition: all ease-in-out 0.3s;
  width: fit-content;

  ${(p) => css`
    margin: ${p.margin || '0 0 22px 0'};
  `}
`;

const Label = styled.label`
  display: flex;
  font-size: ${({ fontSize }) => fontSize || '14px'};
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
