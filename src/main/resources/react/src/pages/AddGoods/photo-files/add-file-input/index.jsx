import { useState } from 'react';
import { useField } from 'formik';

import dropsPng from 'assets/img/drag-n-drop.png';

import { ErrorDisplay } from '../../error-display';
import { FilesLabel, Input, SpanAdd, Image, WrapError } from './styles';

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
    <FilesLabel
      error={error}
      onDrop={(e) => dropHandler(e)}
      onDragOver={(e) => dragStartHandler(e)}
      onDragStart={(e) => dragStartHandler(e)}
      onDragLeave={(e) => dragLeaveHandler(e)}
    >
      <Input
        multiple
        type="file"
        name="file"
        accept=".png, .jpg, .jpeg, .gif"
        onChange={onChange}
      />
      {drag ? <Image src={dropsPng} alt="drop" /> : <SpanAdd />}
      {error && (
        <WrapError>
          <ErrorDisplay error={!!error && error} />
        </WrapError>
      )}
    </FilesLabel>
  );
};

export { AddFileInput };
