import React from 'react';
import { useSelector } from 'react-redux';
import styled from 'styled-components';

import { getTranslatedText } from 'components/local/localization';

const GenderDiv = styled.div`
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin: 3px 0 26px;
`;

const Container = styled.div`
  display: flex;
  width: 415px;
`;

const Label = styled.label`
  font-size: 14px;
  line-height: 16px;
  display: inline-flex;
`;

const ChildDiv = styled.div`
  cursor: pointer;
  margin-right: 41px;
  &:last-child {
    margin-right: 0;
  }
`;

const CircleBoy = styled.span`
  display: inline-flex;
  width: 15px;
  height: 15px;
  border: 4px solid ${({ theme: { colors } }) => colors['btn-blue-normal']};
  border-radius: 50%;
  background-color: ${({ theme: { colors }, gender }) => {
    return gender === 'MALE' ? colors['btn-blue-normal'] : colors['bg-content'];
  }};
  & + span {
    color: ${({ theme: { colors }, gender }) => {
      return gender === 'MALE' && colors['black-color-text'];
    }};
  }
`;

const CircleGirl = styled.span`
  display: inline-flex;
  width: 15px;
  height: 15px;
  border: 4px solid ${({ theme: { colors } }) => colors['btn-blue-normal']};
  border-radius: 50%;
  background-color: ${({ theme: { colors }, gender }) => {
    return gender === 'FEMALE'
      ? colors['btn-blue-normal']
      : colors['bg-content'];
  }};
  & + span {
    color: ${({ theme: { colors }, gender }) => {
      return gender === 'FEMALE' && colors['black-color-text'];
    }};
  }
`;

const CircleUnselected = styled.span`
  display: inline-flex;
  width: 15px;
  height: 15px;
  border: 4px solid ${({ theme: { colors } }) => colors['btn-blue-normal']};
  border-radius: 50%;
  background-color: ${({ theme: { colors }, gender }) => {
    return gender === 'UNSELECTED'
      ? colors['btn-blue-normal']
      : colors['bg-content'];
  }};
  & + span {
    color: ${({ theme: { colors }, gender }) => {
      return gender === 'UNSELECTED' && colors['black-color-text'];
    }};
  }
`;

const Span = styled.span`
  display: inline-flex;
  margin-left: 9px;
  font-size: 16px;
  line-height: 20px;
  vertical-align: bottom;
  color: ${({ theme: { colors } }) => colors.colorGrey};
`;

const InputGender = ({ gender, id, click }) => {
  const { lang } = useSelector((state) => state.auth);
  return (
    <GenderDiv>
      <Label>{getTranslatedText('ownInfo.gender', lang)}</Label>
      <Container>
        <ChildDiv onClick={() => click(id, 'MALE')}>
          <CircleBoy gender={gender} />
          <Span>{getTranslatedText('ownInfo.boy', lang)}</Span>
        </ChildDiv>
        <ChildDiv onClick={() => click(id, 'FEMALE')}>
          <CircleGirl gender={gender} />
          <Span>{getTranslatedText('ownInfo.girl', lang)}</Span>
        </ChildDiv>
        <ChildDiv onClick={() => click(id, 'UNSELECTED')}>
          <CircleUnselected gender={gender} />
          <Span>{getTranslatedText('ownInfo.unselected', lang)}</Span>
        </ChildDiv>
      </Container>
    </GenderDiv>
  );
};

export default InputGender;
