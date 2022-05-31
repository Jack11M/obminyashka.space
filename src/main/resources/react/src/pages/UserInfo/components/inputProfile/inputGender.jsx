/* eslint-disable indent */
import styled, { css } from 'styled-components';
import { useSelector } from 'react-redux';

import { getLang } from 'store/auth/slice';
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
  border-radius: 50%;

  ${({ theme: { colors }, gender }) => css`
    background-color: ${gender === 'MALE'
      ? colors['btn-blue-normal']
      : colors['bg-content']};

    border: 4px solid ${colors['btn-blue-normal']};
    & + span {
      color: ${gender === 'MALE' && colors['black-color-text']};
    }
  `}
`;

const CircleGirl = styled.span`
  display: inline-flex;
  width: 15px;
  height: 15px;
  border-radius: 50%;

  ${({ theme: { colors }, gender }) => css`
    background-color: ${gender === 'FEMALE'
      ? colors['btn-blue-normal']
      : colors['bg-content']};

    border: 4px solid ${colors['btn-blue-normal']};

    & + span {
      color: ${gender === 'FEMALE' && colors['black-color-text']};
    }
  `}
`;

const CircleUnselected = styled.span`
  display: inline-flex;
  width: 15px;
  height: 15px;
  border-radius: 50%;

  ${({ theme: { colors }, gender }) => css`
    background-color: ${gender === 'UNSELECTED'
      ? colors['btn-blue-normal']
      : colors['bg-content']};

    border: 4px solid ${colors['btn-blue-normal']};

    & + span {
      color: ${gender === 'UNSELECTED' && colors['black-color-text']};
    }
  `}
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
  const lang = useSelector(getLang);
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
