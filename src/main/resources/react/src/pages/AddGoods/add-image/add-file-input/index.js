import { useState } from 'react';

import dropsPng from 'assets/img/drag-n-drop.png';
import { FilesLabel, Input, SpanAdd, Image } from './styles';

const AddFileInput = ({ onChange }) => {
  const [drag, setDrag] = useState(false);

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
      onDragStart={(e) => dragStartHandler(e)}
      onDragLeave={(e) => dragLeaveHandler(e)}
      onDragOver={(e) => dragStartHandler(e)}
      onDrop={(e) => dropHandler(e)}
    >
      <Input
        multiple
        type="file"
        name="file"
        accept=".png, .jpg, .jpeg, .gif"
        onChange={onChange}
      />
      {drag ? <Image src={dropsPng} alt="drop" /> : <SpanAdd />}
    </FilesLabel>
  );
};

export { AddFileInput };
