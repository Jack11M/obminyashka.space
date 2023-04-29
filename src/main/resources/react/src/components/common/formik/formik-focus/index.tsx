import { useEffect } from 'react';
import { useFormikContext } from 'formik';

const FormikFocus = () => {
  const { errors, isSubmitting, isValidating } = useFormikContext();

  useEffect(() => {
    if (isSubmitting && !isValidating) {
      const keys = Object.keys(errors);
      const imageIndex = keys.indexOf('images');

      if (keys.includes('images') && imageIndex !== -1) {
        keys.splice(imageIndex, 1);
        keys.push('images');
      }

      if (keys.length > 0) {
        const selector = `[name="${keys[0]}"]`;
        const errorElement = document.querySelector(selector);

        if (errorElement) {
          const scrollToElement = () => {
            window.scrollTo({
              behavior: 'smooth',
              top: errorElement.offsetTop - 200,
            });
          };
          setTimeout(scrollToElement);
        }
      }
    }
  }, [errors, isSubmitting, isValidating]);
  return null;
};

export { FormikFocus };
