import styled, { css } from 'styled-components';

export const TitleBlock = styled.div`
  display: flex;
  justify-content: space-between;
  align-items: center;
  cursor: pointer;
`;

export const Title = styled.span`
  font-size: 18px;
  line-height: 24px;
`;

export const OptionWrapper = styled.div<{ hideSelect: boolean }>`
  flex-direction: column;
  gap: 15px;
  margin: 15px 0;

  ${({ hideSelect }) => css`
    display: ${hideSelect ? 'flex' : 'none'};
  `}
`;

export const SubTitleBlock = styled.div`
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-right: 15px;
  line-height: 17px;
`;

export const RotateRectangle = styled.div<{ openSelect: boolean }>`
  svg {
    transition: all 0.2s linear;
    -webkit-transition: all 0.2s linear;
  }

  ${({ openSelect }) => css`
    ${openSelect &&
    css`
      svg {
        transform: rotate(180deg);
        webkit-transform: rotate(180deg);
      }
    `}
  `}
`;

export const Input = styled.input`
  width: 100%;
  padding: 6px 15px;
  background: #dedede;
  border-radius: 5px;
  border: none;
`;
