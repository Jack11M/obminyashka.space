import { useState } from 'react';
import { useField } from 'formik';

import dropsPng from 'assets/img/drag-n-drop.png';

import { ErrorDisplay } from '../../error-display';
import * as Styles from './styles';

const AddFileInput = ({ onChange }) => {
  const [drag, setDrag] = useState(false);

  const [, meta] = useField({ name: 'images' });
  const { error } = meta;

  function dragStartHandler(e) {
    e.preventDefault();
    setDrag(true);
  }

  function dragLeaveHandler(e) {
    e.preventDefault();
    setDrag(false);
  }

  const dropHandler = (e) => {
    const files = [...e.dataTransfer.files];
    setDrag(false);
    onChange(e, files);
  };

  return (
    <Styles.FilesLabel
      error={error}
      onDrop={(e) => dropHandler(e)}
      onDragOver={(e) => dragStartHandler(e)}
      onDragStart={(e) => dragStartHandler(e)}
      onDragLeave={(e) => dragLeaveHandler(e)}
    >
      <Styles.Input
        multiple
        type="file"
        name="file"
        accept=".png, .jpg, .jpeg, .gif"
        onChange={onChange}
      />
      {drag ? <Styles.Image src={dropsPng} alt="drop" /> : <Styles.SpanAdd />}
      {error && (
        <Styles.WrapError>
          <ErrorDisplay error={!!error && error} />
        </Styles.WrapError>
      )}
    </Styles.FilesLabel>
  );
};

export { AddFileInput };
