import styled from 'styled-components';

export const WrapDescription = styled.div`
  position: relative;
  padding-bottom: 40px;
`;

export const DescriptionText = styled.p`
  margin-bottom: 10px;
  color: ${({ theme }) => theme.colors.colorGrey};
`;

export const TextArea = styled.textarea`
  padding: 10px;
  margin-top: 16px;
  width: 100%;
  min-height: 150px;
  font-size: 16px;
  line-height: 24px;
  outline: none;
  resize: none;
  border: 1px solid #bcbcbc;
  border-radius: 2px;
  caret-color: ${({ theme }) => theme.colors.activeColor};

  &:focus {
    border-color: hsl(0, 0%, 44%);
  }
`;

export const TitleH3 = styled.h3`
  font-family: 'Open Sans', sans-serif;
  font-size: 26px;
  font-weight: 600;
  margin-bottom: 10px;
  line-height: 40px;
`;
