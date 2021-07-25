import { FilesLabel, Input, SpanAdd } from './styles';
const ItemInput = ({ onChange }) => {
  return (
    <FilesLabel>
      <Input
        multiple
        type="file"
        name="file"
        accept=".png, .jpg, .jpeg"
        onChange={onChange}
      />
      <SpanAdd />
    </FilesLabel>
  );
};

export { ItemInput };
