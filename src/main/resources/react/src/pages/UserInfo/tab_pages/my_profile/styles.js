import styled from 'styled-components';

const wrapperInputFlexColumnGap = `
  display: flex;
  flex-direction: column;
  gap: 20px;
`;

export const WrapperInputWhitOutPhones = styled.div`
  margin-bottom: 20px;

  ${wrapperInputFlexColumnGap}
`;

export const WrapperInputPhones = styled.div`
  margin-bottom: 55px;

  ${wrapperInputFlexColumnGap}
`;

export const WrapperInputAddPhones = styled.div`
  position: relative;

  ${wrapperInputFlexColumnGap}
`;

export const WrapperAddButton = styled.div`
  max-width: 150px;
  margin-left: 135px;
`;

export const WrapperDelButton = styled.div`
  position: absolute;
  top: 8px;
  right: -40px;
`;
