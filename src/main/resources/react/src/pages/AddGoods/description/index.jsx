// import { useField } from 'formik';

import { getTranslatedText } from 'components/local';

import * as Styles from './styles';

const Description = ({ value, setDescription }) => {
  //   const [, meta] = useField('description');
  //   console.log(meta);

  const handleDescription = (descriptionValue) => {
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
        onChange={(e) => handleDescription(e.target.value)}
      />
      {/* <div>0/255</div> */}
    </Styles.WrapDescription>
  );
};

export { Description };
