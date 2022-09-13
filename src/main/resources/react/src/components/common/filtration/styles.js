import styled, { css } from 'styled-components';

export const styleSet = css`
  padding: 30px 20px;
  width: 310px;
  border: 2px dashed ${({ theme }) => theme.colors.btnBlue};
  border-radius: 20px;
`;

export const BlockStyleSet = css`
  display: flex;
  flex-direction: column;
  gap: 12px;
  margin-top: 20px;
`;

export const CategoryFilter = styled.div`
  ${styleSet}
`;

export const Filter = styled.div`
  ${styleSet}
  margin-top: 20px;
`;

export const SelectBlock = styled.div`
  ${BlockStyleSet};
`;

export const CheckBoxBlock = styled.div`
  ${BlockStyleSet};
`;

export const Title = styled.div`
  font-weight: 700;
  font-size: 19px;
  line-height: 24px;
  text-transform: uppercase;
  color: ${({ theme }) => theme.colors.btnBlue};
`;
