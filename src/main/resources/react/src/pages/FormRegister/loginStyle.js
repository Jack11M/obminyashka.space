import styled from 'styled-components';
import { Link } from 'react-router-dom';

export const InputDiv = styled.div`
  margin-bottom: ${ ( { path } ) => path ? '42px' : '22px' };

  &:last-child {
    margin-bottom: ${ ( { path } ) => path ? '26px' : '32px' };
  }
`;

export const Label = styled.label`
  font-style: normal;
  font-weight: normal;
  font-size: 14px;
  line-height: 22px;
  color: ${ ( { theme: { colors } } ) => colors.colorGrey };
  cursor: pointer;
`;

export const InputAuth = styled.input`
  padding: 12px 16px;
  width: 100%;
  border-radius: 2px;
  box-sizing: border-box;
  font-family: Roboto, sans-serif;
  font-style: normal;
  font-weight: normal;
  font-size: 16px;
  line-height: 24px;
  outline: none;
  border: 1px solid ${ ( { theme: { colors }, error } ) => error ? colors['colorError']: 'hsl(0, 0%, 74%)' };
  color: ${ ( { theme: { colors } } ) => colors['right-color-text'] };

  &:focus {
    border-color: ${ ( { theme: { colors }, error } ) => error ? colors['colorError']:'hsl(0, 0%, 44%)'};
  }
`;

export const SpanError = styled.span`
  font-size: 12px;
  font-style: normal;
  font-weight: 400;
  line-height: 20px;

  ${ InputAuth } {
    border-color: ${ ( { theme: { colors }, error } ) => error && colors['colorError'] };
  }
;
  color: ${ ( { theme: { colors } } ) => colors['colorError'] };
`;

export const Extra = styled.div`
  display: -webkit-box;
  display: -ms-flexbox;
  display: flex;
  -webkit-box-pack: justify;
  -ms-flex-pack: justify;
  justify-content: space-between;
`;

export const ExtraLink = styled( Link )`
  font-style: normal;
  font-weight: 600;
  font-size: 14px;
  line-height: 17px;
  text-decoration-line: none;
  color: ${ ( { theme: { colors } } ) => colors['btn-blue-normal'] };
`;