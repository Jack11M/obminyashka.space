import { useField } from 'formik';

import { getTranslatedText } from 'components/local';

import * as Styles from './styles';

const maxValue = 255;

const Description = ({ value, setDescription }) => {
  const [, meta, helpers] = useField('description');

  const handleDescription = (descriptionValue) => {
    if (descriptionValue.length > maxValue) {
      helpers.setError('error');
      return;
    }
    setDescription(descriptionValue);
  };

  return (
    <Styles.WrapDescription>
      <Styles.TitleH3>
        {getTranslatedText('addAdv.describeTitle')}
      </Styles.TitleH3>

      <Styles.DescriptionText>
        {getTranslatedText('addAdv.describeText')}
      </Styles.DescriptionText>

      <Styles.TextArea
        value={value}
        error={meta.error}
        onChange={(e) => handleDescription(e.target.value)}
      />
      <Styles.ErrorCount error={meta.error}>
        {`${meta.value.length}/${maxValue}`}
      </Styles.ErrorCount>
    </Styles.WrapDescription>
  );
};

export { Description };
