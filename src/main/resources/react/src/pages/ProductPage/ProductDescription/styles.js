import styled from 'styled-components';

export const Container = styled.div`
  background: #fdfcff;
  box-shadow: 0 0 40px rgba(89, 102, 116, 0.1);
  display: flex;
  flex-wrap: wrap;
  flex-direction: column;
  height: 220px;
`;

export const TitleContainer = styled.div`
  margin-top: 20px;
  margin-left: 34px;
`;

export const Title = styled.h2`
  font-family: 'Roboto', sans-serif;
  font-size: 26px;
  line-height: 40px;
`;

export const DescriptionContainer = styled.div`
  margin: 20px 34px 34px;
  word-break: break-all;
`;

export const DescriptionText = styled.p`
  font-family: 'Roboto', sans-serif;
  font-size: 16px;
  line-height: 26px;
`;
