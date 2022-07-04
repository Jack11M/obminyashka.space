import styled from 'styled-components';

const H2 = styled.h2`
  font-style: normal;
  font-weight: 700;
  font-size: 50px;
  line-height: 24px;
  font-family: Caveat, cursive;
  color: #11b5ed;
  &:before,
  &:after {
    display: inline-block;
    content: '';
    width: 11px;
    height: 11px;
    border-radius: 50%;
    background-color: #11b5ed;
    margin-bottom: 5px;
  }
  &:before {
    margin-right: 27px;
  }
  &:after {
    margin-left: 27px;
  }
`;

const TitleBigBlue = ({ whatClass = '', text, style }) => (
  <H2 className={whatClass} style={style}>
    {text}
  </H2>
);

export default TitleBigBlue;
