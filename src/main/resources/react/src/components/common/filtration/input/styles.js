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

export const OptionWrapper = styled.div`
  flex-direction: column;
  gap: 15px;
  margin: 10px 0;

  ${({ hideSelect }) => css`
    display: ${hideSelect ? 'flex' : 'none'};
  `}
`;

export const SubTitleBlock = styled.div`
  display: flex;
  justify-content: space-between;
  align-items: center;
  line-height: 17px;
`;

export const RotateRectangle = styled.div`
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
  padding: 7px 30px;
  background: #dedede;
  border-radius: 5px;
  border: none;
`;
