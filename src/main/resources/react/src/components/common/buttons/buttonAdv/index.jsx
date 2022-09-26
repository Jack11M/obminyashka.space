import { Loader } from 'components/common';
import { route } from 'routes/routeConstants';
import { getTranslatedText } from 'components/local/localization';

import * as Styles from './styles';

const ButtonAdv = ({ type = 'submit', isLoading }) => {
  return (
    <>
      {type === 'link' && (
        <Styles.StylizedLink to={route.addAdv}>
          <Styles.Span>{getTranslatedText('button.addAdv')}</Styles.Span>
        </Styles.StylizedLink>
      )}

      {type === 'submit' && (
        <Styles.SubmitButton type="submit" isLoading={isLoading}>
          {isLoading ? (
            <Loader />
          ) : (
            <Styles.Span>{getTranslatedText('button.addAdv')}</Styles.Span>
          )}
        </Styles.SubmitButton>
      )}
    </>
  );
};

export { ButtonAdv };
