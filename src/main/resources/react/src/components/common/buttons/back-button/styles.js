import styled, { css } from 'styled-components';

export const Button = styled.button`
  display: flex;
  align-items: center;
  font-weight: 600;

  ${({ theme }) => css`
    color: ${theme.colors['btn-blue-normal']};
  `}
`;

export const Text = styled.span`
  margin: 0 8px;
  line-height: 20px;
`;

export const Icon = styled.span`
  height: 24px;

  ${({ theme }) => css`
    > svg > path {
      fill: ${theme.colors['btn-blue-normal']};
    }
  `}
`;
