/* eslint-disable indent */
import styled, { css } from 'styled-components';

export const GenderDiv = styled.div`
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin: 3px 0 26px;
`;

export const Container = styled.div`
  display: flex;
  width: 415px;
  gap: 40px;
`;

export const Title = styled.label`
  font-size: 14px;
  line-height: 16px;
  display: inline-flex;
`;

export const ChildDiv = styled.div`
  display: flex;
  align-items: center;
  cursor: pointer;
`;

export const Circle = styled.span`
  width: 15px;
  height: 15px;
  border-radius: 50%;
  transition: all 0.3s;

  ${({ theme, selected }) => css`
    border: 4px solid ${theme.colors.btnBlue};
    background-color: ${theme.colors.bgContent};

    ${selected &&
    css`
      background-color: ${theme.colors.btnBlue};

      & + span {
        transition: all 0.3s;
        color: ${theme.colors.blackColorText};
      }
    `}
  `}
`;

export const LabelGander = styled.span`
  margin-left: 8px;
  font-size: 16px;
  line-height: 20px;
  user-select: none;
  color: ${({ theme }) => theme.colors.colorGrey};
`;
