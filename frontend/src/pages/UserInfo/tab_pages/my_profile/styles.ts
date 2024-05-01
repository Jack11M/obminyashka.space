import styled from "styled-components";

const wrapperInputFlexColumnGap = `
  display: flex;
  flex-direction: column;
  gap: 20px;
`;

export const WrapperInputWhitOutPhones = styled.div`
  margin-bottom: 20px;

  ${wrapperInputFlexColumnGap};
`;

export const WrapperInputPhones = styled.div`
  margin-bottom: 55px;

  ${wrapperInputFlexColumnGap};
`;

export const WrapperInputAddPhones = styled.div`
  position: relative;

  ${wrapperInputFlexColumnGap};
`;

export const InputPhonesContainer = styled.div`
  display: flex;
  justify-content: space-between;
  align-items: center;
  width: 100%;

  .react-tel-input {
    width: fit-content;
    border-radius: 0;
  }

  .form-control {
    height: 40px;
    width: 415px;
  }
`;

export const WrapperAddButton = styled.div`
  max-width: 150px;
  margin-left: 135px;
`;

export const WrapperDelButton = styled.div`
  position: absolute;
  top: 4px;
  right: -40px;
`;
